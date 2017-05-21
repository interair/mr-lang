package me.interair.lexer.mr.gui;

import lombok.extern.slf4j.Slf4j;
import me.interair.lexer.mr.MrLexer;
import me.interair.lexer.mr.eval.Evaluator;
import me.interair.lexer.mr.eval.Issue;
import me.interair.lexer.mr.eval.MrEvaluator;
import me.interair.lexer.mr.eval.Result;
import org.antlr.v4.runtime.CharStreams;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.parser.*;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.net.URL;
import java.util.List;

@Slf4j
public class TextEditor extends JFrame {

    private final static Color BACKGROUND = new Color(39, 40, 34);
    private final static Color BACKGROUND_SUBTLE_HIGHLIGHT = new Color(49, 50, 44);
    private final Evaluator evaluator = new MrEvaluator();

    public TextEditor() {

        final JPanel cp = new JPanel(new BorderLayout());
        JLabel statusLabel = new JLabel();
        RSyntaxTextArea rSyntaxTextArea = makeTextPanel(new LanguageSupport() {
            @Override
            public SyntaxScheme getSyntaxScheme() {
                return new DefaultSyntaxScheme();
            }

            @Override
            public AntlrLexerFactory getAntlrLexerFactory() {
                return code -> new MrLexer(CharStreams.fromString(code));
            }

            @Override
            public Validator getValidator() {
                return new MrValidator();
            }

        }, statusLabel);
        RTextScrollPane sp = new RTextScrollPane(rSyntaxTextArea);
        cp.add(sp);

        setLayout(new BorderLayout());

        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));

        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);

        add(cp, BorderLayout.CENTER);
        setTitle("Text Editor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

    }

    private RSyntaxTextArea makeTextPanel(LanguageSupport languageSupport, JLabel statusLabel) {
        final RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);

        ((RSyntaxDocument) textArea.getDocument()).setSyntaxStyle(new AntlrTokenMaker(languageSupport.getAntlrLexerFactory()));

        textArea.setSyntaxScheme(languageSupport.getSyntaxScheme());
        textArea.setCodeFoldingEnabled(true);
        textArea.setBackground(BACKGROUND);
        textArea.setForeground(Color.WHITE);
        textArea.setAnimateBracketMatching(true);
        textArea.setCurrentLineHighlightColor(BACKGROUND_SUBTLE_HIGHLIGHT);
        textArea.addParser(new Parser() {
            @Override
            public ExtendedHyperlinkListener getHyperlinkListener() {
                throw new UnsupportedOperationException("not implemented");
            }

            @Override
            public URL getImageBase() {
                return null;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public ParseResult parse(RSyntaxDocument doc, String style) {
                DefaultParseResult parseResult = new DefaultParseResult(this);
                try {
                    String text = getText(doc);
                    if (StringUtils.hasText(text)) {
                        List<Issue> issues = languageSupport.getValidator().validate(text);
                        issues.forEach(it -> parseResult.addNotice(new DefaultParserNotice(this,
                                it.getMessage(), it.getLine(), it.getOffset(), it.getLength())));
                        if (CollectionUtils.isEmpty(issues)) {
                            Result evaluate = evaluator.evaluate(text);
                            statusLabel.setText(evaluate.getValue().asString());
                        } else {
                            statusLabel.setText(issues.toString());
                        }
                    }

                } catch (Exception e) {
                    log.error("", e);
                    parseResult.setError(e);
                }
                return parseResult;
            }

            private String getText(RSyntaxDocument doc) {
                try {
                    return doc.getText(0, doc.getLength());
                } catch (BadLocationException e) {
                    log.error("Can't fetch text", e);
                    return null;
                }
            }
        });

        return textArea;
    }

}
