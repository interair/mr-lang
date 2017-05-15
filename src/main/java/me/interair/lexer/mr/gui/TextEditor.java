package me.interair.lexer.mr.gui;

import lombok.extern.slf4j.Slf4j;
import me.interair.lexer.mr.MrLexer;
import org.antlr.v4.runtime.CharStreams;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.parser.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.net.URL;
import java.util.List;

@Slf4j
public class TextEditor extends JFrame {

    private final static Color BACKGROUND = new Color(39, 40, 34);
    private final static Color BACKGROUND_SUBTLE_HIGHLIGHT = new Color(49, 50, 44);

    public TextEditor() {

        final JPanel cp = new JPanel(new BorderLayout());

        RTextScrollPane sp = new RTextScrollPane(makeTextPanel(new LanguageSupport() {
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
                return new Validator.EverythingOkValidator();
            }

        }, ""));
        cp.add(sp);

        setContentPane(cp);
        setTitle("Text Editor Demo");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

    }

    private RSyntaxTextArea makeTextPanel(LanguageSupport languageSupport, String initialContext) {
        final RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);

        ((RSyntaxDocument) textArea.getDocument()).setSyntaxStyle(new AntlrTokenMaker(languageSupport.getAntlrLexerFactory()));

        textArea.setSyntaxScheme(languageSupport.getSyntaxScheme());
        textArea.setText(initialContext);
        textArea.setCodeFoldingEnabled(true);
        textArea.setBackground(BACKGROUND);
        textArea.setForeground(Color.WHITE);
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
                return false;
            }

            @Override
            public ParseResult parse(RSyntaxDocument doc, String style) {
                try {
                    List<Issue> issues = languageSupport.getValidator().validate(doc.getText(0, doc.getLength()));
                    DefaultParseResult parseResult = new DefaultParseResult(this);
                    issues.forEach(it ->
                            parseResult.addNotice(new DefaultParserNotice(this,
                                    it.getMessage(), it.getLine(), it.getOffset(), it.getLength())));
                    return parseResult;
                } catch (BadLocationException e) {
                    log.error("", e);
                    throw new RuntimeException(e);
                }
            }
        });

        return textArea;
    }

}
