// Generated from c:\Users\luisr\OneDrive - Universidade do Algarve\u005Cuni\3º Ano\2º SEMESTRE\Compiladores\Trabalho\Part1\src\mar.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LExprLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, NUMBER=4, PRINT=5, INT=6, MULT=7, DIV=8, ADD=9, 
		SUB=10, DOT=11, NEWLINE=12, WS=13;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "NUMBER", "PRINT", "INT", "MULT", "DIV", "ADD", 
			"SUB", "DOT", "NEWLINE", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'('", "')'", null, "'print'", null, "'*'", "'/'", "'+'", 
			"'-'", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, "NUMBER", "PRINT", "INT", "MULT", "DIV", "ADD", 
			"SUB", "DOT", "NEWLINE", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public LExprLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "mar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\17Q\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\3\3\3\3\4\3\4\3\5\5\5%\n\5\3\5"+
		"\3\5\3\5\3\5\3\5\5\5,\n\5\3\5\5\5/\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\6\7"+
		"8\n\7\r\7\16\79\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\5\rG\n\r"+
		"\3\r\3\r\3\16\6\16L\n\16\r\16\16\16M\3\16\3\16\2\2\17\3\3\5\4\7\5\t\6"+
		"\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\3\2\4\3\2\62;\4\2\13\13"+
		"\"\"\2V\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2"+
		"\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3"+
		"\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\3\35\3\2\2\2\5\37\3\2\2\2\7!\3\2\2\2"+
		"\t.\3\2\2\2\13\60\3\2\2\2\r\67\3\2\2\2\17;\3\2\2\2\21=\3\2\2\2\23?\3\2"+
		"\2\2\25A\3\2\2\2\27C\3\2\2\2\31F\3\2\2\2\33K\3\2\2\2\35\36\7=\2\2\36\4"+
		"\3\2\2\2\37 \7*\2\2 \6\3\2\2\2!\"\7+\2\2\"\b\3\2\2\2#%\5\25\13\2$#\3\2"+
		"\2\2$%\3\2\2\2%&\3\2\2\2&\'\5\r\7\2\'(\5\27\f\2()\5\r\7\2)/\3\2\2\2*,"+
		"\5\25\13\2+*\3\2\2\2+,\3\2\2\2,-\3\2\2\2-/\5\r\7\2.$\3\2\2\2.+\3\2\2\2"+
		"/\n\3\2\2\2\60\61\7r\2\2\61\62\7t\2\2\62\63\7k\2\2\63\64\7p\2\2\64\65"+
		"\7v\2\2\65\f\3\2\2\2\668\t\2\2\2\67\66\3\2\2\289\3\2\2\29\67\3\2\2\29"+
		":\3\2\2\2:\16\3\2\2\2;<\7,\2\2<\20\3\2\2\2=>\7\61\2\2>\22\3\2\2\2?@\7"+
		"-\2\2@\24\3\2\2\2AB\7/\2\2B\26\3\2\2\2CD\7\60\2\2D\30\3\2\2\2EG\7\17\2"+
		"\2FE\3\2\2\2FG\3\2\2\2GH\3\2\2\2HI\7\f\2\2I\32\3\2\2\2JL\t\3\2\2KJ\3\2"+
		"\2\2LM\3\2\2\2MK\3\2\2\2MN\3\2\2\2NO\3\2\2\2OP\b\16\2\2P\34\3\2\2\2\t"+
		"\2$+.9FM\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}