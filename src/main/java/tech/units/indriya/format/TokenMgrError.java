/*
 * Units of Measurement Reference Implementation
 * Copyright (c) 2005-2025, Jean-Marie Dautelle, Werner Keil, Otavio Santana.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of JSR-385, Indriya nor the names of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tech.units.indriya.format;

import javax.measure.MeasurementError;

/** Token Manager Error. 
 * @version 2.0
 * @author Werner Keil
 */
public class TokenMgrError extends MeasurementError {

  /**
   * The Serialization identifier for this class. Increment only if the <i>serialized</i> form of the class changes.
   */
  private static final long serialVersionUID = -3348968864772188432L;

  /*
   * Ordinals for various reasons why an Error of this type can be thrown.
   */

  /**
   * Lexical error occurred.
   */
  public static final int LEXICAL_ERROR = 0;

  /**
   * An attempt was made to create a second instance of a static token manager.
   */
  public static final int STATIC_LEXER_ERROR = 1;

  /**
   * Tried to change to an invalid lexical state.
   */
  public static final int INVALID_LEXICAL_STATE = 2;

  /**
   * Detected (and bailed out of) an infinite loop in the token manager.
   */
  public static final int LOOP_DETECTED = 3;

  /**
   * Replaces unprintable characters by their escaped (or unicode escaped) equivalents in the given string
   */
  protected static String addEscapes(String str) {
    StringBuilder retval = new StringBuilder();
    char ch;
    for (int i = 0; i < str.length(); i++) {
      switch (str.charAt(i)) {
        case 0:
          continue;
        case '\b':
          retval.append("\\b");
          continue;
        case '\t':
          retval.append("\\t");
          continue;
        case '\n':
          retval.append("\\n");
          continue;
        case '\f':
          retval.append("\\f");
          continue;
        case '\r':
          retval.append("\\r");
          continue;
        case '\"':
          retval.append("\\\"");
          continue;
        case '\'':
          retval.append("\\\'");
          continue;
        case '\\':
          retval.append("\\\\");
          continue;
        default:
          if ((ch = str.charAt(i)) < 0x20 || ch > 0x7e) {
            String s = "0000" + Integer.toString(ch, 16);
            retval.append("\\u").append(s.substring(s.length() - 4, s.length()));
          } else {
            retval.append(ch);
          }
      }
    }
    return retval.toString();
  }

  /**
   * Returns a detailed message for the Error when it is thrown by the token manager to indicate a lexical error. Parameters : EOFSeen : indicates if
   * EOF caused the lexical error curLexState : lexical state in which this error occurred errorLine : line number when the error occurred errorColumn
   * : column number when the error occurred errorAfter : prefix that was seen before this error occurred curchar : the offending character Note: You
   * can customize the lexical error message by modifying this method.
   */
  protected static String lexicalError(boolean EOFSeen, int errorLine, int errorColumn, String errorAfter, char curChar) {
    return ("Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: "
        + (EOFSeen ? "<EOF> " : ("\"" + addEscapes(String.valueOf(curChar)) + "\"") + " (" + (int) curChar + "), ") + "after : \""
        + addEscapes(errorAfter) + "\"");
  }

  /*
   * Constructors of various flavors follow.
   */

  /** No arg constructor. */
  public TokenMgrError() {
  }
  
  /** Constructor with message. */
  public TokenMgrError(String message) {
    super(message);
  }

  /** Constructor with message and reason. */
  public TokenMgrError(String message, int reason) {
    super(message);
    @SuppressWarnings("unused")
	int errorCode = reason; // TODO use?
  }

  /** Full Constructor. */
  public TokenMgrError(boolean EOFSeen, int errorLine, int errorColumn, String errorAfter, char curChar, int reason) {
    this(lexicalError(EOFSeen, errorLine, errorColumn, errorAfter, curChar), reason);
  }
}
/*
 * JavaCC - OriginalChecksum=8a6e5be586cca28053ad55584e013006 (do not edit this
 * line)
 */
