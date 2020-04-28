/* Generated By:JavaCC: Do not edit this line. PseuParserTokenManager.java */
package pseu.parser;
import pseu.common.SourceCoords ;
import pseu.common.Assert ;

/** Token Manager. */
public class PseuParserTokenManager implements PseuParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x7fffffc0L) != 0L)
         {
            jjmatchedKind = 48;
            return 21;
         }
         if ((active0 & 0xc0000000000L) != 0L)
         {
            jjmatchedKind = 47;
            return 17;
         }
         if ((active0 & 0x80000000L) != 0L)
            return 17;
         return -1;
      case 1:
         if ((active0 & 0x210400L) != 0L)
            return 21;
         if ((active0 & 0xc0000000000L) != 0L)
            return 17;
         if ((active0 & 0x7fdefbc0L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 1;
            return 21;
         }
         return -1;
      case 2:
         if ((active0 & 0x3018d240L) != 0L)
            return 21;
         if ((active0 & 0x4fc62980L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 2;
            return 21;
         }
         return -1;
      case 3:
         if ((active0 & 0x3400800L) != 0L)
            return 21;
         if ((active0 & 0x4c862180L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 3;
            return 21;
         }
         return -1;
      case 4:
         if ((active0 & 0x4c002080L) != 0L)
            return 21;
         if ((active0 & 0x860100L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 4;
            return 21;
         }
         return -1;
      case 5:
         if ((active0 & 0x800100L) != 0L)
            return 21;
         if ((active0 & 0x60000L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 5;
            return 21;
         }
         return -1;
      case 6:
         if ((active0 & 0x40000L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 6;
            return 21;
         }
         if ((active0 & 0x20000L) != 0L)
            return 21;
         return -1;
      case 7:
         if ((active0 & 0x40000L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 7;
            return 21;
         }
         return -1;
      case 8:
         if ((active0 & 0x40000L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 8;
            return 21;
         }
         return -1;
      case 9:
         if ((active0 & 0x40000L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 9;
            return 21;
         }
         return -1;
      case 10:
         if ((active0 & 0x40000L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 10;
            return 21;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 40:
         return jjStopAtPos(0, 36);
      case 41:
         return jjStopAtPos(0, 37);
      case 42:
         return jjStartNfaWithStates_0(0, 31, 17);
      case 44:
         return jjStopAtPos(0, 32);
      case 45:
         return jjMoveStringLiteralDfa1_0(0x40000000000L);
      case 46:
         return jjStopAtPos(0, 35);
      case 58:
         jjmatchedKind = 34;
         return jjMoveStringLiteralDfa1_0(0x10000000000000L);
      case 59:
         return jjStopAtPos(0, 33);
      case 60:
         return jjMoveStringLiteralDfa1_0(0x80000000000L);
      case 91:
         return jjStopAtPos(0, 38);
      case 93:
         return jjStopAtPos(0, 39);
      case 97:
         return jjMoveStringLiteralDfa1_0(0x40L);
      case 98:
         return jjMoveStringLiteralDfa1_0(0x180L);
      case 100:
         return jjMoveStringLiteralDfa1_0(0x600L);
      case 101:
         return jjMoveStringLiteralDfa1_0(0x1800L);
      case 102:
         return jjMoveStringLiteralDfa1_0(0xe000L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x70000L);
      case 109:
         return jjMoveStringLiteralDfa1_0(0x80000L);
      case 110:
         return jjMoveStringLiteralDfa1_0(0x100000L);
      case 111:
         return jjMoveStringLiteralDfa1_0(0x200000L);
      case 112:
         return jjMoveStringLiteralDfa1_0(0x400000L);
      case 114:
         return jjMoveStringLiteralDfa1_0(0x800000L);
      case 116:
         return jjMoveStringLiteralDfa1_0(0x3000000L);
      case 117:
         return jjMoveStringLiteralDfa1_0(0xc000000L);
      case 118:
         return jjMoveStringLiteralDfa1_0(0x30000000L);
      case 119:
         return jjMoveStringLiteralDfa1_0(0x40000000L);
      case 123:
         return jjStopAtPos(0, 40);
      case 125:
         return jjStopAtPos(0, 41);
      default :
         return jjMoveNfa_0(2, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 45:
         if ((active0 & 0x80000000000L) != 0L)
            return jjStartNfaWithStates_0(1, 43, 17);
         break;
      case 61:
         if ((active0 & 0x10000000000000L) != 0L)
            return jjStopAtPos(1, 52);
         break;
      case 62:
         if ((active0 & 0x40000000000L) != 0L)
            return jjStartNfaWithStates_0(1, 42, 17);
         break;
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x30002000L);
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x800080L);
      case 102:
         if ((active0 & 0x10000L) != 0L)
            return jjStartNfaWithStates_0(1, 16, 21);
         break;
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x41000000L);
      case 105:
         return jjMoveStringLiteralDfa2_0(active0, 0x300L);
      case 108:
         return jjMoveStringLiteralDfa2_0(active0, 0x800L);
      case 109:
         return jjMoveStringLiteralDfa2_0(active0, 0x20000L);
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0xc041040L);
      case 111:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(1, 10, 21);
         return jjMoveStringLiteralDfa2_0(active0, 0x184000L);
      case 114:
         if ((active0 & 0x200000L) != 0L)
            return jjStartNfaWithStates_0(1, 21, 21);
         return jjMoveStringLiteralDfa2_0(active0, 0x2400000L);
      case 117:
         return jjMoveStringLiteralDfa2_0(active0, 0x8000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa3_0(active0, 0x4000000L);
      case 100:
         if ((active0 & 0x40L) != 0L)
            return jjStartNfaWithStates_0(2, 6, 21);
         else if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(2, 12, 21);
         else if ((active0 & 0x80000L) != 0L)
            return jjStartNfaWithStates_0(2, 19, 21);
         break;
      case 101:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000000L);
      case 103:
         return jjMoveStringLiteralDfa3_0(active0, 0x80L);
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0x48000000L);
      case 108:
         if ((active0 & 0x10000000L) != 0L)
            return jjStartNfaWithStates_0(2, 28, 21);
         return jjMoveStringLiteralDfa3_0(active0, 0x2000L);
      case 110:
         if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_0(2, 15, 21);
         return jjMoveStringLiteralDfa3_0(active0, 0x100L);
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x400000L);
      case 112:
         return jjMoveStringLiteralDfa3_0(active0, 0x20000L);
      case 114:
         if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(2, 14, 21);
         else if ((active0 & 0x20000000L) != 0L)
            return jjStartNfaWithStates_0(2, 29, 21);
         break;
      case 115:
         return jjMoveStringLiteralDfa3_0(active0, 0x800L);
      case 116:
         if ((active0 & 0x100000L) != 0L)
            return jjStartNfaWithStates_0(2, 20, 21);
         return jjMoveStringLiteralDfa3_0(active0, 0x840000L);
      case 117:
         return jjMoveStringLiteralDfa3_0(active0, 0x2000000L);
      case 118:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(2, 9, 21);
         break;
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa4_0(active0, 0x100L);
      case 99:
         if ((active0 & 0x400000L) != 0L)
            return jjStartNfaWithStates_0(3, 22, 21);
         break;
      case 101:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(3, 11, 21);
         else if ((active0 & 0x2000000L) != 0L)
            return jjStartNfaWithStates_0(3, 25, 21);
         return jjMoveStringLiteralDfa4_0(active0, 0x40000L);
      case 105:
         return jjMoveStringLiteralDfa4_0(active0, 0x80L);
      case 108:
         return jjMoveStringLiteralDfa4_0(active0, 0x40020000L);
      case 110:
         if ((active0 & 0x1000000L) != 0L)
            return jjStartNfaWithStates_0(3, 24, 21);
         break;
      case 111:
         return jjMoveStringLiteralDfa4_0(active0, 0x8000000L);
      case 114:
         return jjMoveStringLiteralDfa4_0(active0, 0x4000000L);
      case 115:
         return jjMoveStringLiteralDfa4_0(active0, 0x2000L);
      case 117:
         return jjMoveStringLiteralDfa4_0(active0, 0x800000L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x2000L) != 0L)
            return jjStartNfaWithStates_0(4, 13, 21);
         else if ((active0 & 0x40000000L) != 0L)
            return jjStartNfaWithStates_0(4, 30, 21);
         break;
      case 105:
         return jjMoveStringLiteralDfa5_0(active0, 0x20000L);
      case 110:
         if ((active0 & 0x80L) != 0L)
            return jjStartNfaWithStates_0(4, 7, 21);
         else if ((active0 & 0x8000000L) != 0L)
            return jjStartNfaWithStates_0(4, 27, 21);
         break;
      case 114:
         return jjMoveStringLiteralDfa5_0(active0, 0x840100L);
      case 121:
         if ((active0 & 0x4000000L) != 0L)
            return jjStartNfaWithStates_0(4, 26, 21);
         break;
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa6_0(active0, 0x20000L);
      case 110:
         if ((active0 & 0x800000L) != 0L)
            return jjStartNfaWithStates_0(5, 23, 21);
         break;
      case 115:
         return jjMoveStringLiteralDfa6_0(active0, 0x40000L);
      case 121:
         if ((active0 & 0x100L) != 0L)
            return jjStartNfaWithStates_0(5, 8, 21);
         break;
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private int jjMoveStringLiteralDfa6_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa7_0(active0, 0x40000L);
      case 115:
         if ((active0 & 0x20000L) != 0L)
            return jjStartNfaWithStates_0(6, 17, 21);
         break;
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
private int jjMoveStringLiteralDfa7_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(5, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 99:
         return jjMoveStringLiteralDfa8_0(active0, 0x40000L);
      default :
         break;
   }
   return jjStartNfa_0(6, active0);
}
private int jjMoveStringLiteralDfa8_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(6, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(7, active0);
      return 8;
   }
   switch(curChar)
   {
      case 116:
         return jjMoveStringLiteralDfa9_0(active0, 0x40000L);
      default :
         break;
   }
   return jjStartNfa_0(7, active0);
}
private int jjMoveStringLiteralDfa9_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(7, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(8, active0);
      return 9;
   }
   switch(curChar)
   {
      case 105:
         return jjMoveStringLiteralDfa10_0(active0, 0x40000L);
      default :
         break;
   }
   return jjStartNfa_0(8, active0);
}
private int jjMoveStringLiteralDfa10_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(8, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(9, active0);
      return 10;
   }
   switch(curChar)
   {
      case 111:
         return jjMoveStringLiteralDfa11_0(active0, 0x40000L);
      default :
         break;
   }
   return jjStartNfa_0(9, active0);
}
private int jjMoveStringLiteralDfa11_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(9, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(10, active0);
      return 11;
   }
   switch(curChar)
   {
      case 110:
         if ((active0 & 0x40000L) != 0L)
            return jjStartNfaWithStates_0(11, 18, 21);
         break;
      default :
         break;
   }
   return jjStartNfa_0(10, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec2 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 21;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 2:
                  if ((0xf000ac7a00000000L & l) != 0L)
                  {
                     if (kind > 47)
                        kind = 47;
                     jjCheckNAdd(17);
                  }
                  else if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 44)
                        kind = 44;
                     jjCheckNAddTwoStates(4, 5);
                  }
                  else if (curChar == 34)
                     jjCheckNAddStates(0, 2);
                  if (curChar == 47)
                     jjstateSet[jjnewStateCnt++] = 0;
                  break;
               case 21:
                  if ((0x83ff000200000000L & l) != 0L)
                     jjCheckNAddTwoStates(19, 20);
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 48)
                        kind = 48;
                  }
                  break;
               case 0:
                  if (curChar != 47)
                     break;
                  if (kind > 5)
                     kind = 5;
                  jjCheckNAdd(1);
                  break;
               case 1:
                  if ((0xffffffffffffdbffL & l) == 0L)
                     break;
                  if (kind > 5)
                     kind = 5;
                  jjCheckNAdd(1);
                  break;
               case 3:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 44)
                     kind = 44;
                  jjCheckNAddTwoStates(4, 5);
                  break;
               case 4:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(4, 5);
                  break;
               case 5:
                  if ((0x3ff000000000000L & l) != 0L && kind > 44)
                     kind = 44;
                  break;
               case 6:
                  if (curChar == 34)
                     jjCheckNAddStates(0, 2);
                  break;
               case 7:
                  if ((0xfffffffbffffdbffL & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 9:
                  if ((0x8400000000L & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 10:
                  if (curChar == 34 && kind > 45)
                     kind = 45;
                  break;
               case 11:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddStates(3, 6);
                  break;
               case 12:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 13:
                  if ((0xf000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 14;
                  break;
               case 14:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAdd(12);
                  break;
               case 17:
                  if ((0xf000ac7a00000000L & l) == 0L)
                     break;
                  if (kind > 47)
                     kind = 47;
                  jjCheckNAdd(17);
                  break;
               case 19:
                  if ((0x83ff000200000000L & l) != 0L)
                     jjCheckNAddTwoStates(19, 20);
                  break;
               case 20:
                  if ((0x3ff000000000000L & l) != 0L && kind > 48)
                     kind = 48;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 2:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 48)
                        kind = 48;
                     jjCheckNAddTwoStates(19, 20);
                  }
                  else if ((0x50000000d0000001L & l) != 0L)
                  {
                     if (kind > 47)
                        kind = 47;
                     jjCheckNAdd(17);
                  }
                  if (curChar == 92)
                     jjCheckNAdd(16);
                  break;
               case 21:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                     jjCheckNAddTwoStates(19, 20);
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 48)
                        kind = 48;
                  }
                  break;
               case 1:
                  if (kind > 5)
                     kind = 5;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 4:
                  if (curChar == 95)
                     jjAddStates(7, 8);
                  break;
               case 7:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 8:
                  if (curChar == 92)
                     jjAddStates(9, 11);
                  break;
               case 9:
                  if ((0x14404410000000L & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 15:
                  if (curChar == 92)
                     jjCheckNAdd(16);
                  break;
               case 16:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 46)
                     kind = 46;
                  jjCheckNAdd(16);
                  break;
               case 17:
                  if ((0x50000000d0000001L & l) == 0L)
                     break;
                  if (kind > 47)
                     kind = 47;
                  jjCheckNAdd(17);
                  break;
               case 18:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 48)
                     kind = 48;
                  jjCheckNAddTwoStates(19, 20);
                  break;
               case 19:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                     jjCheckNAddTwoStates(19, 20);
                  break;
               case 20:
                  if ((0x7fffffe07fffffeL & l) != 0L && kind > 48)
                     kind = 48;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 1:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 5)
                     kind = 5;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 7:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                     jjAddStates(0, 2);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 21 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   7, 8, 10, 7, 8, 12, 10, 4, 5, 9, 11, 13, 
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec2[i2] & l2) != 0L);
      default :
         if ((jjbitVec0[i1] & l1) != 0L)
            return true;
         return false;
   }
}

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, "\141\156\144", "\142\145\147\151\156", 
"\142\151\156\141\162\171", "\144\151\166", "\144\157", "\145\154\163\145", "\145\156\144", 
"\146\141\154\163\145", "\146\157\162", "\146\165\156", "\151\146", "\151\155\160\154\151\145\163", 
"\151\156\164\145\162\163\145\143\164\151\157\156", "\155\157\144", "\156\157\164", "\157\162", "\160\162\157\143", 
"\162\145\164\165\162\156", "\164\150\145\156", "\164\162\165\145", "\165\156\141\162\171", 
"\165\156\151\157\156", "\166\141\154", "\166\141\162", "\167\150\151\154\145", "\52", "\54", "\73", 
"\72", "\56", "\50", "\51", "\133", "\135", "\173", "\175", "\55\76", "\74\55", null, 
null, null, null, null, null, null, null, "\72\75", null, };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0x31ffffffffffc1L, 
};
static final long[] jjtoSkip = {
   0x3eL, 
};
protected JavaCharStream input_stream;
private final int[] jjrounds = new int[21];
private final int[] jjstateSet = new int[42];
protected char curChar;
/** Constructor. */
public PseuParserTokenManager(JavaCharStream stream){
   if (JavaCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public PseuParserTokenManager(JavaCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(JavaCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 21; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(JavaCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedPos == 0 && jjmatchedKind > 53)
   {
      jjmatchedKind = 53;
   }
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}
