package database;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Serialize {
	
	public SEXP data;
	public boolean problem=false;
	public String emessage = "";
	
	public class SEXP {
		int type;
		int levels;
		boolean objf;
		public int[] nVals;
		public double[] dVals;
		boolean[] bVals;
		byte[] byVals;
		Rcomplex[] cVals;
		String character = "";
		String[] sVals;
		String[] cNames;
		String[] rNames;
		String slot = "";
		public List<SEXP> list;
		SEXP attr;
		SEXP tag;
		
		SEXP(int type, int length) {
			this.type = type;
			switch(this.type) {
			case NILSXP:	  /* nil = NULL */
			case SYMSXP:	  /* symbols */
			case LISTSXP:	  /* lists of dotted pairs */
			case CLOSXP:	  /* closures */
			case ENVSXP:	  /* environments */
			case PROMSXP:	  /* promises: [un]evaluated closure arguments */
			case LANGSXP:	  /* language constructs (special lists) */
			case SPECIALSXP:	  /* special forms */
			case BUILTINSXP:	  /* builtin non-special forms */
				break;
			case CHARSXP:	  /* "scalar" string type (internal only)*/
				break;
			case LGLSXP:	  /* logical vectors */
				bVals = new boolean[length];
				break;
			/* 11 and 12 were factors and ordered factors in the 1990s */
			case INTSXP:	  /* integer vectors */
				nVals = new int[length];
				break;
			case REALSXP:	  /* real variables */
				dVals = new double[length];
				break;
			case CPLXSXP:	  /* complex variables */
				cVals = new Rcomplex[length];
				break;
			case STRSXP:	  /* string vectors */
				sVals = new String[length];
				break;
			case DOTSXP:	  /* dot-dot-dot object */
			case ANYSXP:
				break;
			case VECSXP:	  /* generic vectors */
				list = new ArrayList<Serialize.SEXP>(length);
				break;
			case EXPRSXP:	  /* expressions vectors */
				break;
			case BCODESXP:    /* byte code */
				byVals = new byte[length];
				break;
			case EXTPTRSXP:    /* external pointer */
				list = new ArrayList<Serialize.SEXP>(length);
				break;
			case WEAKREFSXP:    /* weak reference */
				break;
			case RAWSXP:    /* raw bytes */
				byVals = new byte[length];
				break;
			case S4SXP:    /* S4, non-vector */
			case NEWSXP:    /* fresh node creaed in new page */
			case FREESXP:    /* node released by GC */
			case FUNSXP:    /* Closure or Builtin or Special */
			default:
				break;
			}
		}
		
		public int LENGTH() {
			switch(this.type) {
			case NILSXP:	  /* nil = NULL */
				return 0;
			case SYMSXP:	  /* symbols */
				return 0;
			case LISTSXP:	  /* lists of dotted pairs */
				return 0;
			case CLOSXP:	  /* closures */
				return 0;
			case ENVSXP:	  /* environments */
				return 0;
			case PROMSXP:	  /* promises: [un]evaluated closure arguments */
				return 0;
			case LANGSXP:	  /* language constructs (special lists) */
				return 0;
			case SPECIALSXP:	  /* special forms */
				return 0;
			case BUILTINSXP:	  /* builtin non-special forms */
				return 0;
			case CHARSXP:	  /* "scalar" string type (internal only)*/
				return 1;
			case LGLSXP:	  /* logical vectors */
				return bVals.length;
			/* 11 and 12 were factors and ordered factors in the 1990s */
			case INTSXP:	  /* integer vectors */
				return nVals.length;
			case REALSXP:	  /* real variables */
				return dVals.length;
			case CPLXSXP:	  /* complex variables */
				return cVals.length;
			case STRSXP:	  /* string vectors */
				return sVals.length;
			case DOTSXP:	  /* dot-dot-dot object */
				return 0;
			case ANYSXP:	  /* make "any" args work.
						     Used in specifying types for symbol
						     registration to mean anything is okay  */
				return 0;
			case VECSXP:	  /* generic vectors */
				return list.size();
			case EXPRSXP:	  /* expressions vectors */
				return 0;
			case BCODESXP:    /* byte code */
				return byVals.length;
			case EXTPTRSXP:    /* external pointer */
				return list.size();
			case WEAKREFSXP:    /* weak reference */
				return 0;
			case RAWSXP:    /* raw bytes */
				return byVals.length;
			case S4SXP:    /* S4, non-vector */
				return 0;
			/* used for detecting PROTECT issues in memory.c */
			case NEWSXP:    /* fresh node creaed in new page */
				return 0;
			case FREESXP:    /* node released by GC */
				return 0;
			case FUNSXP:    /* Closure or Builtin or Special */
				return 0;
			default:
				return 0;
			}
		}
		public String toString() {
			int len = this.LENGTH();
			String val = getType(this.type);
			if(len != 0)
				val += " " + Integer.toString(len);
			return val;
		}
	};
	
	public class Rcomplex {
		double r;
		double i;
	};
	
	private class R_outpstream_st {
		//Output Buffer Stream
		ByteArrayOutputStream R_outpstream_t;
	    R_pstream_format_t type;
	    int version;
	};

	public class R_inpstream_st {
		//Input Data Buffer
		ByteBuffer serialized;
	    R_pstream_format_t type;
	};
	
	class SEXPINFO {
		public int type;
		public int levs;
		public boolean objf;
		public boolean hasattr;
		public boolean hastag;
	};
	
	private R_outpstream_st R_outpstream_t;
	private R_inpstream_st R_inpstream_t;
	
	
	static int R_DefaultSerializeVersion = 2;
	static int EOF = -1;
	static int NA_INTEGER = 0;
	static double NA_REAL = 0;
	
	private int R_ReadItemDepth = 0;
	private int R_InitReadItemDepth = 0;
	private String lastname;
	
	
	static final int NILSXP = 0;	  /* nil = NULL */
	static final int SYMSXP = 1;	  /* symbols */
	static final int LISTSXP = 2;	  /* lists of dotted pairs */
	static final int CLOSXP = 3;	  /* closures */
	static final int ENVSXP = 4;	  /* environments */
	static final int PROMSXP = 5;	  /* promises: [un]evaluated closure arguments */
	static final int LANGSXP = 6;	  /* language constructs (special lists) */
	static final int SPECIALSXP = 7;	  /* special forms */
	static final int BUILTINSXP = 8;	  /* builtin non-special forms */
	static final int CHARSXP = 9;	  /* "scalar" string type (internal only)*/
	static final int LGLSXP = 10;	  /* logical vectors */
	/* 11 and 12 were factors and ordered factors in the 1990s */
	static final int INTSXP = 13;	  /* integer vectors */
	static final int REALSXP = 14;	  /* real variables */
	static final int CPLXSXP = 15;	  /* complex variables */
	static final int STRSXP = 16;	  /* string vectors */
	static final int DOTSXP = 17;	  /* dot-dot-dot object */
	static final int ANYSXP = 18;	  /* make "any" args work.
				     Used in specifying types for symbol
				     registration to mean anything is okay  */
	static final int VECSXP = 19;	  /* generic vectors */
	static final int EXPRSXP = 20;	  /* expressions vectors */
	static final int BCODESXP = 21;    /* byte code */
	static final int EXTPTRSXP = 22;    /* external pointer */
	static final int WEAKREFSXP = 23;    /* weak reference */
	static final int RAWSXP = 24;    /* raw bytes */
	static final int S4SXP = 25;    /* S4, non-vector */

	/* used for detecting PROTECT issues in memory.c */
	static final int NEWSXP = 30;    /* fresh node creaed in new page */
	static final int FREESXP = 31;    /* node released by GC */

	static final int FUNSXP = 99;    /* Closure or Builtin or Special */
	
	/*
	 * Administrative SXP values
	 *
	 * These macros defind SXP "type" for specifying special object, such
	 * as R_NilValue, or control information, like REFSXP or NAMESPACESXP.
	 * The range of SXP types is limited to 5 bit by the current sxpinfo
	 * layout, but just in case these values are placed at the top of the
	 * 8 bit range.
	 */

	static final int REFSXP = 255;
	static final int NILVALUE_SXP=254;
	static final int GLOBALENV_SXP=253;
	static final int UNBOUNDVALUE_SXP=252;
	static final int MISSINGARG_SXP=251;
	static final int BASENAMESPACE_SXP=250;
	static final int NAMESPACESXP=249;
	static final int PACKAGESXP=248;
	static final int PERSISTSXP=247;
	/* the following are speculative--we may or may not need them soon */
	static final int CLASSREFSXP=246;
	static final int GENERICREFSXP=245;
	static final int BCREPDEF=244;
	static final int BCREPREF=243;
	static final int EMPTYENV_SXP=242;
	static final int BASEENV_SXP=241;
	/* The following are needed to preserve attribute information on
	 expressions in the constant pool of byte code objects. This is
	 mainly for preserving source references attributes.  The original
	 implementation of the sharing-preserving writing and reading of yte
	 code objects did not account for the need to preserve attributes,
	 so there is now a work-around using these SXP types to flag when
	 the ATTRIB field has been written out. Object bits and S4 bits are
	 still not preserved.  It the long run in might be better to change
	 to a scheme in which all sharing is preserved and byte code objects
	 don't need to be handled as a special case.  LT */
	static int ATTRLANGSXP = 240;
	static int ATTRLISTSXP = 239;

	
	public enum R_pstream_format_t {
	    R_pstream_any_format,
	    R_pstream_ascii_format,
	    R_pstream_binary_format,
	    R_pstream_xdr_format
	}
	
	
	int _ISupper = ((0) < 8 ? ((1 << (0)) << 8) : ((1 << (0)) >> 8));	/* UPPERCASE.  */
	int _ISlower = ((1) < 8 ? ((1 << (1)) << 8) : ((1 << (1)) >> 8));	/* lowercase.  */
	int _ISalpha = ((2) < 8 ? ((1 << (2)) << 8) : ((1 << (2)) >> 8));	/* Alphabetic.  */
	int _ISdigit = ((3) < 8 ? ((1 << (3)) << 8) : ((1 << (3)) >> 8));	/* Numeric.  */
	int _ISxdigit = ((4) < 8 ? ((1 << (4)) << 8) : ((1 << (4)) >> 8));	/* Hexadecimal numeric.  */
	int _ISspace = ((5) < 8 ? ((1 << (5)) << 8) : ((1 << (5)) >> 8));	/* Whitespace.  */
	int _ISprint = ((6) < 8 ? ((1 << (6)) << 8) : ((1 << (6)) >> 8));	/* Printing.  */
	int _ISgraph = ((7) < 8 ? ((1 << (7)) << 8) : ((1 << (7)) >> 8));	/* Graphical.  */
	int _ISblank = ((8) < 8 ? ((1 << (8)) << 8) : ((1 << (8)) >> 8));	/* Blank (usually SPC and TAB).  */
	int _IScntrl = ((9) < 8 ? ((1 << (9)) << 8) : ((1 << (9)) >> 8));	/* Control character.  */
	int _ISpunct = ((10) < 8 ? ((1 << (10)) << 8) : ((1 << (10)) >> 8));	/* Punctuation.  */
	int _ISalnum = ((11) < 8 ? ((1 << (11)) << 8) : ((1 << (11)) >> 8));	/* Alphanumeric.  */
	
	
	private boolean __isctype(byte c, int type) {
	  return false;//needs some work
	}
	private boolean isspace(byte c) {
		return __isctype(c, _ISspace);
	}
	private void error(String error) {
		System.out.println(error);
	}
	
	/*
	 * Basic Output Routines
	 */

	private void OutInteger(R_outpstream_st stream, int i)
	{
		//char buf[128];
		/*switch (stream.type) {
		case R_pstream_ascii_format:
			if (i == NA_INTEGER)
				Rsnprintf(buf, sizeof(buf), "NA\n");
			else
				Rsnprintf(buf, sizeof(buf), "%d\n", i);
			stream->OutBytes(stream, buf, (int) strlen(buf));
			break;
		case R_pstream_binary_format:
			stream->OutBytes(stream, &i, sizeof(int));
			break;
		case R_pstream_xdr_format:
			R_XDREncodeInteger(i, buf);
			stream->OutBytes(stream, buf, R_XDR_INTEGER_SIZE);
			break;
		default:
			error(_("unknown or inappropriate output format"));
		}*/
	}

	private void OutReal(R_outpstream_st stream, double d) {
		/*char buf[128];
		switch (stream->type) {
		case R_pstream_ascii_format:
			if (!R_FINITE(d)) {
				if (ISNAN(d))
					Rsnprintf(buf, sizeof(buf), "NA\n");
				else if (d < 0)
					Rsnprintf(buf, sizeof(buf), "-Inf\n");
				else
					Rsnprintf(buf, sizeof(buf), "Inf\n");
			} else
				// 16: full precision; 17 gives 999, 000 &c //
				Rsnprintf(buf, sizeof(buf), "%.16g\n", d);
			stream->OutBytes(stream, buf, (int) strlen(buf));
			break;
		case R_pstream_binary_format:
			stream->OutBytes(stream, &d, sizeof(double));
			break;
		case R_pstream_xdr_format:
			R_XDREncodeDouble(d, buf);
			stream->OutBytes(stream, buf, R_XDR_DOUBLE_SIZE);
			break;
		default:
			error(_("unknown or inappropriate output format"));
		}*/
	}

	private void OutComplex(R_outpstream_st stream, Rcomplex c) {
		//OutReal(stream, c.r);
		//OutReal(stream, c.i);
	}

	private void OutByte(R_outpstream_st stream, byte i) {
		/*char buf[128];
		switch (stream->type) {
		case R_pstream_ascii_format:
			Rsnprintf(buf, sizeof(buf), "%02x\n", i);
			stream->OutBytes(stream, buf, (int) strlen(buf));
			break;
		case R_pstream_binary_format:
		case R_pstream_xdr_format:
			stream->OutBytes(stream, &i, 1);
			break;
		default:
			error(_("unknown or inappropriate output format"));
		}*/
	}

	/* This assumes CHARSXPs remain limited to 2^31-1 bytes */
	private void OutString(R_outpstream_st stream, byte s, int length) {
		/*if (stream->type == R_pstream_ascii_format) {
			int i;
			char buf[128];
			for (i = 0; i < length; i++) {
				switch (s[i]) {
				case '\n':
					sprintf(buf, "\\n");
					break;
				case '\t':
					sprintf(buf, "\\t");
					break;
				case '\v':
					sprintf(buf, "\\v");
					break;
				case '\b':
					sprintf(buf, "\\b");
					break;
				case '\r':
					sprintf(buf, "\\r");
					break;
				case '\f':
					sprintf(buf, "\\f");
					break;
				case '\a':
					sprintf(buf, "\\a");
					break;
				case '\\':
					sprintf(buf, "\\\\");
					break;
				case '\?':
					sprintf(buf, "\\?");
					break;
				case '\'':
					sprintf(buf, "\\'");
					break;
				case '\"':
					sprintf(buf, "\\\"");
					break;
				default:
					// cannot print char in octal mode -> cast to unsigned
					// char first //
					// actually, since s is signed char and '\?' == 127
					// is handled above, s[i] > 126 can't happen, but
					// I'm superstitious...  -pd //
					if (s[i] <= 32 || s[i] > 126)
						sprintf(buf, "\\%03o", (unsigned char) s[i]);
					else
						sprintf(buf, "%c", s[i]);
				}
				stream->OutBytes(stream, buf, (int) strlen(buf));
			}
			stream->OutChar(stream, '\n');
		} else
			stream->OutBytes(stream, (void *) s, length); /* FIXME: is this case right? */
	}

	/*
	 * Basic Input Routines
	 */

	private void InWord(R_inpstream_st stream, byte[] buf, int size) {
		byte c;
		int i;
		i = 0;
		do {
			c = stream.serialized.get();
			if (c == EOF)
				error("read error");
		} while (isspace(c));
		while (!isspace(c) && i < size) {
			buf[i++] = c;
			c = stream.serialized.get();
		}
		if (i == size)
			error("read error");
		buf[i] = 0;
	}

	private int InInteger(R_inpstream_st stream) {
		//byte[] word = new byte[128];
		//byte[] buf = new byte[128];
		//int i;

		switch (stream.type) {
		case R_pstream_ascii_format:
			//InWord(stream, word, 1);
			//sscanf(word, "%s", buf);
			//if (strcmp(buf, "NA") == 0)
			//	return NA_INTEGER;
			//else
			//	sscanf(buf, "%d", &i);
			//return i;
			return NA_INTEGER;
		case R_pstream_binary_format:
			//stream->InBytes(stream, &i, sizeof(int));
			//return i;
			return NA_INTEGER;
		case R_pstream_xdr_format:
			return stream.serialized.getInt();
		default:
			return NA_INTEGER;
		}
	}

	private double InReal(R_inpstream_st stream) {
		//char word[128];
		//char buf[128];
		//double d;

		switch (stream.type) {
		case R_pstream_ascii_format:
			//InWord(stream, word, sizeof(word));
			//sscanf(word, "%s", buf);
			//if (strcmp(buf, "NA") == 0)
			//	return NA_REAL;
			//else if (strcmp(buf, "Inf") == 0)
			//	return R_PosInf;
			//else if (strcmp(buf, "-Inf") == 0)
			//	return R_NegInf;
			//else
			//	sscanf(buf, "%lg", &d);
			return NA_REAL;
		case R_pstream_binary_format:
			//stream->InBytes(stream, &d, sizeof(double));
			return NA_REAL;
		case R_pstream_xdr_format:
			return stream.serialized.getDouble();
		default:
			return NA_REAL;
		}
	}

	private Rcomplex InComplex(R_inpstream_st stream) {
		Rcomplex c = new Rcomplex();
		c.r = InReal(stream);
		c.i = InReal(stream);
		return c;
	}
	
	private void InString(R_inpstream_st stream, byte[] buf, int length) {
		if (stream.type == R_pstream_format_t.R_pstream_ascii_format) {
			
		} else {
			stream.serialized.get(buf, 0, length);
		}
	}
	
	private void InFormat(R_inpstream_st stream) {
		byte[] buf = new byte[2];
		R_pstream_format_t type;
		stream.serialized.get(buf, 0, 2);
		String format = "";
		
		try {
			format = new String(new byte[] { buf[0] }, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		switch (format) {
		case "A":
			type = R_pstream_format_t.R_pstream_ascii_format;
			break;
		case "B":
			type = R_pstream_format_t.R_pstream_binary_format;
			break;
		case "X":
			type = R_pstream_format_t.R_pstream_xdr_format;
			break;
		case "\n":
			/* GROSS HACK: ASCII unserialize may leave a trailing newline
			 in the stream.  If the stream contains a second
			 serialization, then a second unserialize will fail if such
			 a newline is present.  The right fix is to make sure
			 unserialize consumes exactly what serialize produces.  But
			 this seems hard because of the current use of whitespace
			 skipping in unserialize.  So a temporary hack to cure the
			 symptom is to deal with a possible leading newline.  I
			 don't think more than one is possible, but I'm not sure.
			 LT */
			if (buf[1] == 'A') {
				type = R_pstream_format_t.R_pstream_ascii_format;
				stream.serialized.get();
				break;
			}
		default:
			type = R_pstream_format_t.R_pstream_any_format; /* keep compiler happy */
			error("unknown input format");
		}
		if (stream.type == R_pstream_format_t.R_pstream_any_format)
			stream.type = type;
		else if (type != stream.type)
			error("input format does not match specified format");
	}
	
	

	private SEXP MakeReadRefTable() {
		SEXP data = new SEXP(VECSXP, 128);
		return data;
	}

	private SEXP GetReadRef(SEXP table, int index) {
		int i = index - 1;
		
		if (i < 0 || i >= table.LENGTH())
			error("reference index out of range");
		return table;
	}

	private void AddReadRef(SEXP table, SEXP value) {
		/*SEXP data = table;
		int count = TRUELENGTH(data) + 1;
		if (count >= LENGTH(data)) {
			int i, len;
			SEXP newdata;

			PROTECT(value);
			len = 2 * count;
			newdata = allocVector(VECSXP, len);
			for (i = 0; i < LENGTH(data); i++)
				SET_VECTOR_ELT(newdata, i, VECTOR_ELT(data, i));
			SETCAR(table, newdata);
			data = newdata;
			UNPROTECT(1);
		}
		SET_TRUELENGTH(data, count);
		SET_VECTOR_ELT(data, count - 1, value);*/
	}

	private SEXP InStringVec(R_inpstream_st stream, SEXP ref_table) {
		SEXP s;
		int i, len;
		if (InInteger(stream) != 0)
			error("names in persistent strings are not supported yet");
		len = InInteger(stream);
		s = new SEXP(STRSXP, len);
		R_ReadItemDepth++;
		for (i = 0; i < len; i++)
			s.sVals[i] =  ReadItem(ref_table, stream).sVals[0];
		R_ReadItemDepth--;
		return s;
	}

	/* use static buffer to reuse storage */
	private void InIntegerVec(R_inpstream_st stream, SEXP obj, int length) {
		switch (stream.type) {
		case R_pstream_xdr_format: {
			obj.nVals = new int[length];
			for (int cnt = 0; cnt < length; cnt++) {
				obj.nVals[cnt] = stream.serialized.getInt();
			}
			break;
		}
		case R_pstream_binary_format: {
			//R_xlen_t done, this;
			//for (done = 0; done < length; done += this) {
			//	this = min2(CHUNK_SIZE, length - done);
			//	stream->InBytes(stream, INTEGER(obj) + done,
			//			(int) (sizeof(int) * this));
			//}
			break;
		}
		default:
			//for (R_xlen_t cnt = 0; cnt < length; cnt++)
			//	INTEGER(obj)[cnt] = InInteger(stream);
			break;
		}
	}

	private void InRealVec(R_inpstream_st stream, SEXP obj, int length) {
		switch (stream.type) {
		case R_pstream_xdr_format: {
			obj.dVals = new double[length];
			for(int cnt=0; cnt<length; cnt++) {
				obj.dVals[cnt] = stream.serialized.getDouble();
			}
			break;
		}
		case R_pstream_binary_format: {
			//R_xlen_t done, this;
			//for (done = 0; done < length; done += this) {
			//	this = min2(CHUNK_SIZE, length - done);
			//	stream->InBytes(stream, REAL(obj) + done,
			//			(int) (sizeof(double) * this));
			//}
			break;
		}
		default:
			//for (R_xlen_t cnt = 0; cnt < length; cnt++)
			//	REAL(obj)[cnt] = InReal(stream);
			break;
		}
	}

	private void InComplexVec(R_inpstream_st stream, SEXP obj, int length) {
		switch (stream.type) {
		case R_pstream_xdr_format: {
			obj.cVals = new Rcomplex[length];
			for(int cnt=0; cnt<length; cnt++) {
				obj.cVals[cnt].i = stream.serialized.getDouble();
				obj.cVals[cnt].r = stream.serialized.getDouble();
			}
			break;
		}
		case R_pstream_binary_format: {
			//R_xlen_t done, this;
			//for (done = 0; done < length; done += this) {
			//	this = min2(CHUNK_SIZE, length - done);
			//	stream->InBytes(stream, COMPLEX(obj) + done,
			//			(int) (sizeof(Rcomplex) * this));
			//}
			break;
		}
		default:
			//for (R_xlen_t cnt = 0; cnt < length; cnt++)
			//	COMPLEX(obj)[cnt] = InComplex(stream);
		}
	}

	private int ReadLENGTH(R_inpstream_st stream) {
		int len = InInteger(stream);
		if (len < -1)
			error("negative serialized length for vector");
		if (len == -1) {
			//int len1, len2;
			//len1 = InInteger(stream); /* upper part */
			//len2 = InInteger(stream); /* lower part */
			//R_xlen_t xlen = len1;
			/* sanity check for now */
			//if (len1 > 65536)
			error ("invalid upper part of serialized vector length");
			return 0;
		} else return len;
	}

	/* differs when it fails from version in envir.c */
	private SEXP R_FindNamespace1(SEXP info) {
		SEXP expr, val, where;
		//PROTECT(info);
		//where = PROTECT(ScalarString(mkChar(lastname)));
		//PROTECT(
		//		expr = LCONS(install("..getNamespace"),
		//				LCONS(info, LCONS(where, R_NilValue))));
		//val = eval(expr, R_GlobalEnv);
		//UNPROTECT(3);
		return new SEXP(NAMESPACESXP, 0);
	}
	
	public class R_Version {
		int v = 0;
		int p = 0;
		int s = 0;
	}
	
	private void DecodeVersion(int packed, R_Version rv) {
		rv.v = packed / 65536;
		packed = packed % 65536;
		rv.p = packed / 256;
		packed = packed % 256;
		rv.s = packed;
	}
	
	public void unserialize(ByteBuffer b) {
		
		int version;
		int writer_version, release_version;
		SEXP ref_table;
		
		R_inpstream_st stream = new R_inpstream_st();
		stream.serialized = b;
		stream.type = R_pstream_format_t.R_pstream_any_format;

		InFormat(stream);

		/* Read the version numbers */
		version = InInteger(stream);
		writer_version = InInteger(stream);
		release_version = InInteger(stream);
		switch (version) {
		case 2:
			break;
		default:
			if (version != 2) {
				R_Version rv = new R_Version();
				DecodeVersion(writer_version, rv);
				if (release_version < 0)
					error("cannot read unreleased workspace version "+version+" written by experimental R "+rv.v+"."+rv.p+"."+rv.s);
				else {
					int vm, pm, sm;
					DecodeVersion(release_version, rv);
					error("cannot read unreleased workspace version "+version+" written by experimental R "+rv.v+"."+rv.p+"."+rv.s+" or newer");
				}
			}
		}

		/* Read the actual object back */
		ref_table = MakeReadRefTable();
		data = new SEXP(LISTSXP, 1);
		data.list = new ArrayList<Serialize.SEXP>();
		while(stream.serialized.remaining() != 0) {
			SEXP temp = ReadItem(ref_table, stream);
			if(this.problem == true)
				break;
			if(temp.type == REALSXP || temp.type == INTSXP)
				data.list.add(temp);
		}
	}
	
	public String getType(int type) {
		switch(type) {
		case NILVALUE_SXP: //NILVALUE_SXP
			return "NILVALUE_SXP";
		case EMPTYENV_SXP: //EMPTYENV_SXP
			return "EMPTYENV_SXP";
		case BASEENV_SXP: //BASEENV_SXP
			return "BASEENV_SXP";
		case GLOBALENV_SXP: //GLOBALENV_SXP
			return "GLOBALENV_SXP";
		case UNBOUNDVALUE_SXP: //UNBOUNDVALUE_SXP
			return "UNBOUNDVALUE_SXP";
		case MISSINGARG_SXP: //MISSINGARG_SXP
			return "MISSINGARG_SXP";
		case BASENAMESPACE_SXP: //BASENAMESPACE_SXP
			return "BASENAMESPACE_SXP";
		case REFSXP: //REFSXP
			return "REFSXP";//GetReadRef(ref_table, InRefIndex(stream, flags));
		case PERSISTSXP: //PERSISTSXP
			return "PERSISTSXP";
		case SYMSXP: //SYMSXP
			return "SYMSXP";
		case PACKAGESXP: //PACKAGESXP
			return "PACKAGESXP";
		case NAMESPACESXP: //NAMESPACESXP
			return "NAMESPACESXP";
		case ENVSXP: //ENVSXP
			return "ENVSXP";
		case LISTSXP: //LISTSXP
			return "LISTSXP";
		case LANGSXP: //LANGSXP
			return "LANGSXP";
		case CLOSXP: //CLOSXP
			return "CLOSXP";
		case PROMSXP: //PROMSXP
			return "PROMSXP";
		case DOTSXP: //DOTSXP
			return "DOTSXP";
		case EXTPTRSXP: //EXTPTRSXP
			return "EXTPTRSXP";
		case WEAKREFSXP: //WEAKREFSXP
			return "WEAKREFSXP";
		case SPECIALSXP: //SPECIALSXP
			return "SPECIALSXP";
		case BUILTINSXP:
			return "BUILTINSXP";
		case CHARSXP: //CHARSXP
			return "CHARSXP";
		case LGLSXP: //LGLSXP
			return "LGLSXP";
		case INTSXP: //INTSXP
			return "INTSXP";
		case REALSXP: //REALSXP
			return "REALSXP";
		case CPLXSXP: //CPLXSXP
			return "CPLXSXP";
		case STRSXP: //STRSXP
			return "STRSXP";
		case VECSXP: //VECSXP
			return "VECSXP";
		case EXPRSXP: //EXPRSXP
			return "EXPRSXP";
		case BCODESXP: //BCODESXP
			return "BCODESXP";
		case CLASSREFSXP: //CLASSREFSXP
			return "CLASSREFSXP";
		case GENERICREFSXP: //GENERICREFSXP
			return "GENERICREFSXP";
		case RAWSXP: //RAWSXP
			return "RAWSXP";
		case S4SXP: //S4SXP
			return "S4SXP";
		default:
			return Integer.toString(type);
		}
	}
	
	public void UnpackFlags(int flags, SEXPINFO obj) {
		obj.type = ((flags) & 255);
		obj.levs = ((flags) >> 12);
		obj.objf = ((flags & (1 << 8)) == 0) ? true : false;
		obj.hasattr = ((flags & (1 << 9)) == 0) ? true : false;
		obj.hastag = ((flags & (1 << 10)) == 0) ? true : false;
	}
	
	private int InRefIndex(R_inpstream_st stream, int flags) {
		int i = ((flags) >> 8);
		if (i == 0)
			return InInteger(stream);
		else
			return i;
	}
	
	static int streamfull=0;
	
	private SEXP ReadItem(SEXP ref_table, R_inpstream_st stream) {
		int type;
		SEXP s = new SEXP(0,0), temp;
		int len, count;
		int flags, length;
		SEXPINFO sobj = new SEXPINFO();
		
//		if(stream.serialized.remaining() == 0) {
//			System.out.println("Stream is out of data."+streamfull++);
//			return s;
//		}
		
		flags = InInteger(stream);
		UnpackFlags(flags, sobj);
		
		type=sobj.type;
		
		switch (type) {
		case NILVALUE_SXP:
			return new SEXP(NILVALUE_SXP, 0);
		case EMPTYENV_SXP:
			return new SEXP(EMPTYENV_SXP, 0);
		case BASEENV_SXP:
			return new SEXP(BASEENV_SXP, 0);
		case GLOBALENV_SXP:
			return new SEXP(GLOBALENV_SXP, 0);
		case UNBOUNDVALUE_SXP:
			return new SEXP(UNBOUNDVALUE_SXP, 0);
		case MISSINGARG_SXP:
			return new SEXP(MISSINGARG_SXP, 0);
		case BASENAMESPACE_SXP:
			return new SEXP(BASENAMESPACE_SXP, 0);
		case REFSXP:
			return new SEXP(REFSXP, 0);//GetReadRef(ref_table, InRefIndex(stream, flags));
		case PERSISTSXP:
			s = InStringVec(stream, ref_table);
			//s = PersistentRestore(stream, s);
			//UNPROTECT(1);
			//AddReadRef(ref_table, s);
			return s;
		case SYMSXP:
			R_ReadItemDepth++;
			temp = ReadItem(ref_table, stream); /* print name */
			R_ReadItemDepth--;
			s = new SEXP(SYMSXP, 0);
			s.slot = temp.character;
			//AddReadRef(ref_table, s);
			//UNPROTECT(1);
			return s;
		case PACKAGESXP:
			temp = InStringVec(stream, ref_table);
			s = new SEXP(PACKAGESXP, 0);
			s.list = new ArrayList<Serialize.SEXP>();
			s.list.add(temp);
			//s = R_FindPackageEnv(s);
			AddReadRef(ref_table, s);
			return s;
		case NAMESPACESXP:
			s = InStringVec(stream, ref_table);
			s = R_FindNamespace1(s);
			AddReadRef(ref_table, s);
			return s;
		case ENVSXP: {
			int locked = InInteger(stream);

			s = new SEXP(ENVSXP, 0);

			/* MUST register before filling in */
			AddReadRef(ref_table, s);

			/* Now fill it in  */
			R_ReadItemDepth++;
			//s.list = new ArrayList<SEXP>();
			//s.list.add(ReadItem(ref_table, stream)); //SET_ENCLOS
			//s.list.add(ReadItem(ref_table, stream)); //SET_FRAME
			//s.list.add(ReadItem(ref_table, stream)); //SET_HASHTAB
			//s.list.add(ReadItem(ref_table, stream)); //SET_ATTRIB
			R_ReadItemDepth--;
			//if (s.list.get(3).type != NILVALUE_SXP && getAttrib(s, R_ClassSymbol) != R_NilValue)
			//	SET_OBJECT(s, 1);
			//R_RestoreHashCount(s);
			//if (locked == 0)
			//	R_LockEnvironment(s, FALSE);
			/* Convert a NULL enclosure to baseenv() */
			//if (ENCLOS(s) == R_NilValue)
			//	SET_ENCLOS(s, R_BaseEnv);
			//UNPROTECT(1);
			return s;
		}
		case LISTSXP:
		case LANGSXP:
		case CLOSXP:
		case PROMSXP:
		case DOTSXP:
			/* This handling of dotted pair objects still uses recursion
			 on the CDR and so will overflow the PROTECT stack for long
			 lists.  The save format does permit using an iterative
			 approach; it just has to pass around the place to write the
			 CDR into when it is allocated.  It's more trouble than it
			 is worth to write the code to handle this now, but if it
			 becomes necessary we can do it without needing to change
			 the save format. */
			s = new SEXP(type, 0);
			s.levels = sobj.levs;
			s.objf = sobj.objf;
			R_ReadItemDepth++;
			//s.attr = sobj.hasattr ? ReadItem(ref_table, stream) : new SEXP(NILVALUE_SXP, 0);
			//s.tag = sobj.hastag ? ReadItem(ref_table, stream) : new SEXP(NILVALUE_SXP, 0);

			//s.list = new ArrayList<serialize.SEXP>();
			//s.list.add(ReadItem(ref_table, stream));
			R_ReadItemDepth--; /* do this early because of the recursion. */
			//s.list.add(ReadItem(ref_table, stream));
			/* For reading closures and promises stored in earlier versions, convert NULL env to baseenv() */
			//if (type == CLOSXP && CLOENV(s) == R_NilValue)
			//	SET_CLOENV(s, R_BaseEnv);
			//else if (type == PROMSXP && PRENV(s) == R_NilValue)
			//	SET_PRENV(s, R_BaseEnv);
			//UNPROTECT(1); /* s */
			return s;
		default:
			/* These break out of the switch to have their ATTR,
			 LEVELS, and OBJECT fields filled in.  Each leaves the
			 newly allocated value PROTECTed */
			switch (type) {
			case EXTPTRSXP:
				s = new SEXP(type, 0);
				AddReadRef(ref_table, s);
				//R_SetExternalPtrAddr(s, null);
				R_ReadItemDepth++;
				//s.list = new ArrayList<serialize.SEXP>();
				//s.list.add(ReadItem(ref_table, stream));
				//s.list.add(ReadItem(ref_table, stream));
				R_ReadItemDepth--;
				break;
			case WEAKREFSXP:
				//PROTECT(s = R_MakeWeakRef(R_NilValue, R_NilValue, R_NilValue,
				//FALSE));
				//AddReadRef(ref_table, s);
				break;
			case SPECIALSXP:
			case BUILTINSXP: {
				/* These are all short strings */
				length = InInteger(stream);
				byte[] cbuf = new byte[length];
				InString(stream, cbuf, length);
				
				String sTemp = "";
				try {
					sTemp = new String(cbuf, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				s = new SEXP(BUILTINSXP,0);
				s.sVals = new String[1];
				s.sVals[0] = sTemp;
			}
				break;
			case CHARSXP:
				/* Let us suppose these will still be limited to 2^31 -1 bytes */
				length = InInteger(stream);
				if (length == -1)
					s = new SEXP(CHARSXP, 1);
				else {
					byte[] cbuf = new byte[length];
					InString(stream, cbuf, length);

					String sTemp = "";
					try {
						sTemp = new String(cbuf, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
					s = new SEXP(CHARSXP, 1);
					s.character = sTemp;
				}
				break;
			case LGLSXP:
			case INTSXP:
				len = ReadLENGTH(stream);
				s = new SEXP(type, len);
				InIntegerVec(stream, s, len);
				break;
			case REALSXP:
				len = ReadLENGTH(stream);
				s = new SEXP(type, len);
				InRealVec(stream, s, len);
				break;
			case CPLXSXP:
				len = ReadLENGTH(stream);
				s = new SEXP(type, len);
				InComplexVec(stream, s, len);
				break;
			case STRSXP:
				len = ReadLENGTH(stream);
				s = new SEXP(type, len);
				R_ReadItemDepth++;
				for (count = 0; count < len; ++count) {
					s.sVals[count] = ReadItem(ref_table, stream).character;
					//check for Error
					if(s.sVals[count].contains("Error") || s.sVals[count].contains("(converted from warning)")) {
						//JOptionPane.showMessageDialog(null, s.sVals[count]);
						this.problem = true;
						this.emessage = s.sVals[count];
					}
				}
				R_ReadItemDepth--;
				break;
			case VECSXP:
			case EXPRSXP:
				len = ReadLENGTH(stream);
				s = new SEXP(type, len);
				R_ReadItemDepth++;
				//for (count = 0; count < len; ++count) {
				//	if (R_ReadItemDepth <= 0)
				//		System.out.println("R_ReadItemDepth <= 0");
				//	s.list.add(ReadItem(ref_table, stream));
				//}
				R_ReadItemDepth--;
				break;
			case BCODESXP:
				s = new SEXP(type, 0);
				//s = ReadBC(ref_table, stream);
				break;
			case CLASSREFSXP:
				error("this version of R cannot read class references");
			case GENERICREFSXP:
				error("this version of R cannot read generic function references");
			case RAWSXP:
				len = ReadLENGTH(stream);
				s = new SEXP(type, len);
				//{
				//	int done, thiss;
				//	for (done = 0; done < len; done += thiss) {
				//		thiss = min2(CHUNK_SIZE, len - done);
				//		stream->InBytes(stream, RAW(s) + done, (int) this);
				//	}
				//}
				break;
			case S4SXP:
				s = new SEXP(type, 0);
				break;
			default:
				s = new SEXP(NILVALUE_SXP, 0); /* keep compiler happy */
				error("ReadItem: unknown type %i, perhaps written by later version of R");
			}
			if (type != CHARSXP)
				s.levels = sobj.levs;
			s.objf = sobj.objf;
			if (s.type == CHARSXP) {
				/* With the CHARSXP cache maintained through the ATTRIB
				 field that field has already been filled in by the
				 mkChar/mkCharCE call above, so we need to leave it
				 alone.  If there is an attribute (as there might be if
				 the serialized data was created by an older version) we
				 read and ignore the value. */
				R_ReadItemDepth++;
				if (sobj.hasattr)
					//ReadItem(ref_table, stream);
				R_ReadItemDepth--;
			} else {
				R_ReadItemDepth++;
				//s.attr = sobj.hasattr ? ReadItem(ref_table, stream) : new SEXP(NILVALUE_SXP, 0);
				R_ReadItemDepth--;
			}
			return s;
		}
	}

		/*ByteBuffer b = stream.serialized;
		Object s = new Object();
		
		List<Object> ltemp;
		int len = 0;
		if(b.remaining() == 0)
			return s;
		int flags = b.getInt();
		SEXPINFO sobj = new SEXPINFO();
		UnpackFlags(flags, sobj);
		
		switch (sobj.type) {
		case 254: //NILVALUE_SXP
			return "NILVALUE_SXP";
		case 242: //EMPTYENV_SXP
			return "EMPTYENV_SXP";
		case 241: //BASEENV_SXP
			return "BASEENV_SXP";
		case 253: //GLOBALENV_SXP
			return "GLOBALENV_SXP";
		case 252: //UNBOUNDVALUE_SXP
			return "UNBOUNDVALUE_SXP";
		case 251: //MISSINGARG_SXP
			return "MISSINGARG_SXP";
		case 250: //BASENAMESPACE_SXP
			return "BASENAMESPACE_SXP";
		case 255: //REFSXP
			return "REFSXP";//GetReadRef(ref_table, InRefIndex(stream, flags));
		case 247: //PERSISTSXP
			//PROTECT(s = InStringVec(stream, ref_table));
			//s = PersistentRestore(stream, s);
			//UNPROTECT(1);
			//AddReadRef(ref_table, s);
			return "PERSISTSXP";
		case 1: //SYMSXP
			s = new ArrayList<Object>();
			ltemp = (ArrayList<Object>) s;
			ltemp.add(ReadItem(b));
			//R_ReadItemDepth++;
			//PROTECT(s = ReadItem(ref_table, stream));  print name 
			//R_ReadItemDepth--;
			//s = install(CHAR(s));
			//AddReadRef(ref_table, s);
			//UNPROTECT(1);
			return s;
		case 248: //PACKAGESXP
			//PROTECT(s = InStringVec(stream, ref_table));
			//s = R_FindPackageEnv(s);
			//UNPROTECT(1);
			//AddReadRef(ref_table, s);
			return "PACKAGESXP";
		case 249: //NAMESPACESXP
			//PROTECT(s = InStringVec(stream, ref_table));
			//s = R_FindNamespace1(s);
			//AddReadRef(ref_table, s);
			//UNPROTECT(1);
			return "NAMESPACESXP";//s;
		case 4: { //ENVSXP
			//int locked = InInteger(stream);

			//PROTECT(s = allocSExp(ENVSXP));

			 MUST register before filling in 
			//AddReadRef(ref_table, s);

			 Now fill it in  
			//R_ReadItemDepth++;
			//SET_ENCLOS(s, ReadItem(ref_table, stream));
			//SET_FRAME(s, ReadItem(ref_table, stream));
			//SET_HASHTAB(s, ReadItem(ref_table, stream));
			//SET_ATTRIB(s, ReadItem(ref_table, stream));
			//R_ReadItemDepth--;
			//if (ATTRIB(s) != R_NilValue
			//		&& getAttrib(s, R_ClassSymbol) != R_NilValue)
				 We don't write out the object bit for environments,
				 so reconstruct it here if needed. 
			//	SET_OBJECT(s, 1);
			//R_RestoreHashCount(s);
			//if (locked)
			//	R_LockEnvironment(s, FALSE);
			 //Convert a NULL enclosure to baseenv()// 
			//if (ENCLOS(s) == R_NilValue)
			//	SET_ENCLOS(s, R_BaseEnv);
			//UNPROTECT(1);
			return "ENVSXP";//s;
		}
		case 2: //LISTSXP
		case 6: //LANGSXP
		case 3: //CLOSXP
		case 5: //PROMSXP
		case 17: //DOTSXP
			 This handling of dotted pair objects still uses recursion
			 on the CDR and so will overflow the PROTECT stack for long
			 lists.  The save format does permit using an iterative
			 approach; it just has to pass around the place to write the
			 CDR into when it is allocated.  It's more trouble than it
			 is worth to write the code to handle this now, but if it
			 becomes necessary we can do it without needing to change
			 the save format.
			s = new ArrayList<Object>();
			ltemp = (ArrayList<Object>) s;
			ltemp.add(ReadItem(b));
			
			//PROTECT(s = allocSExp(type));
			//SETLEVELS(s, levs);
			//SET_OBJECT(s, objf);
			//R_ReadItemDepth++;
			if(sobj.hasattr)
				ltemp.add(ReadItem(b));
			//SET_ATTRIB(s, hasattr ? ReadItem(ref_table, stream) : R_NilValue);
			if(sobj.hastag)
				ltemp.add(ReadItem(b));
			//SET_TAG(s, hastag ? ReadItem(ref_table, stream) : R_NilValue);
			//if (hastag && R_ReadItemDepth == R_InitReadItemDepth + 1
			//		&& isSymbol(TAG(s))) {
			//	snprintf(lastname, 8192, "%s", CHAR(PRINTNAME(TAG(s))));
			//}
			//if (hastag && R_ReadItemDepth <= 0) {
			//	Rprintf("%*s", 2 * (R_ReadItemDepth - R_InitReadItemDepth), "");
			//	PrintValue(TAG(s));
			//}
			ltemp.add(ReadItem(b));
			//SETCAR(s, ReadItem(ref_table, stream));
			//R_ReadItemDepth--; do this early because of the recursion.
			ltemp.add(ReadItem(b));
			//SETCDR(s, ReadItem(ref_table, stream));
			 For reading closures and promises stored in earlier versions, convert NULL env to baseenv()
			//if (type == CLOSXP && CLOENV(s) == R_NilValue)
			//	SET_CLOENV(s, R_BaseEnv);
			//else if (type == PROMSXP && PRENV(s) == R_NilValue)
			//	SET_PRENV(s, R_BaseEnv);
			//UNPROTECT(1);  s 
			return s;
		default:
			 These break out of the switch to have their ATTR,
			 LEVELS, and OBJECT fields filled in.  Each leaves the
			 newly allocated value PROTECTed
			switch (sobj.type) {
			case 22: //EXTPTRSXP
				//PROTECT(s = allocSExp(type));
				//AddReadRef(ref_table, s);
				//R_SetExternalPtrAddr(s, NULL);
				//R_ReadItemDepth++;
				////R_SetExternalPtrProtected(s, ReadItem(ref_table, stream));
				////R_SetExternalPtrTag(s, ReadItem(ref_table, stream));
				//R_ReadItemDepth--;
				break;
			case 23: //WEAKREFSXP
				//PROTECT(s = R_MakeWeakRef(R_NilValue, R_NilValue, R_NilValue,FALSE));
				//AddReadRef(ref_table, s);
				break;
			case 7: //SPECIALSXP
			case 8: { //BUILTINSXP
				 These are all short strings 
				//length = InInteger(stream);
				//char cbuf[length + 1];
				//InString(stream, cbuf, length);
				//cbuf[length] = '\0';
				//int index = StrToInternal(cbuf);
				//if (index == NA_INTEGER) {
				//	warning(_("unrecognized internal function name \"%s\""), cbuf);
				//	PROTECT(s = R_NilValue);
				//} else
				//	PROTECT(s = mkPRIMSXP(index, type == BUILTINSXP));
			}
				break;
			case 9: //CHARSXP
				 Let us suppose these will still be limited to 2^31 -1 bytes
				int length = b.getInt();
				if (length == -1) {
					s = "";
				} 
				
				String enc;
				byte[] buf = new byte[length];
				b.get(buf, 0, length);
					
				if ((sobj.levs & (1<<3)) == 0)
					enc = "UTF-8";
				else if ((sobj.levs & (1<<2)) == 0)
					enc = "ISO-8859-1";
				else if ((sobj.levs & (1<<1)) == 0)
					enc = "UTF-16";
					
				try {
					s = new String(buf, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				System.out.println((String)s);
				break;
			case 10: //LGLSXP
			case 13: //INTSXP
				len = b.getInt();
				s = new int[len];
				int[] temp = (int[])s;
				for (int cnt = 0; cnt < len; cnt++) {
					temp[cnt] = b.getInt();
					System.out.println(temp[cnt]);
				}
				break;
			case 14: //REALSXP
				len = b.getInt();
				System.out.println("REALSXP Length: "+len);
				s = new double[len];
				double[] dtemp = (double[])s;
				for(int cnt=0; cnt<len; cnt++) {
					dtemp[cnt] = b.getDouble();
				}
				break;
			case 15: //CPLXSXP
				//len = ReadLENGTH(stream);
				//PROTECT(s = allocVector(type, len));
				//InComplexVec(stream, s, len);
				break;
			case 16: //STRSXP
				len = b.getInt();
				s = new ArrayList<String>();
				ltemp = (ArrayList<Object>) s;
				for (int count = 0; count < len; ++count)
					ltemp.add(ReadItem(b));
				
				break;
			case 19: //VECSXP
			case 20: //EXPRSXP
				len = b.getInt();
				s = new ArrayList<Object>();
				ltemp = (ArrayList<Object>) s;
				for (int count = 0; count < len; ++count) {
					ltemp.add(ReadItem(b));
				}
				break;
			case 21: //BCODESXP
				//PROTECT(s = ReadBC(ref_table, stream));
				break;
			case 246: //CLASSREFSXP
				//error(_("this version of R cannot read class references"));
			case 245: //GENERICREFSXP
				//error(
				//		_(
				//				"this version of R cannot read generic function references"));
			case 24: //RAWSXP
				//len = ReadLENGTH(stream);
				//PROTECT(s = allocVector(type, len));
				//{
				//	R_xlen_t done, this;
				//	for (done = 0; done < len; done += this) {
				//		this = min2(CHUNK_SIZE, len - done);
				//		stream->InBytes(stream, RAW(s) + done, (int) this);
				//	}
				//}
				break;
			case 25: //S4SXP
				//PROTECT(s = allocS4Object());
				break;
			default:
				//s = R_NilValue;  keep compiler happy 
				//error(
				//		_(
				//				"ReadItem: unknown type %i, perhaps written by later version of R"),
				//		type);
			}
			//if (type != CHARSXP)
			//	SETLEVELS(s, levs);
			//SET_OBJECT(s, objf);
			if (sobj.type == 9) {
				 With the CHARSXP cache maintained through the ATTRIB
				 field that field has already been filled in by the
				 mkChar/mkCharCE call above, so we need to leave it
				 alone.  If there is an attribute (as there might be if
				 the serialized data was created by an older version) we
				 read and ignore the value. 
				
				if (sobj.hasattr)
					System.out.println(ReadItem(b));
				
			} else {
				if(sobj.hasattr)
					System.out.println(ReadItem(b));
			}
			return s;
		}
	}*/
}
