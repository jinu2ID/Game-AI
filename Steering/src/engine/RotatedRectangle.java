package engine;

/**
 *
 * Adapted from the C++ version writteh by Oren Becker:
 * http://www.ragestorm.net/tutorial?id=22
 */
public class RotatedRectangle {
    public static class _Vector2D {
        double x,y;
        
        void add(_Vector2D v) {
            x+=v.x;
            y+=v.y;
        }

        void sub(_Vector2D v) {
            x-=v.x;
            y-=v.y;
        }
        
        void rotate(double ang) {
            double t;
            double cosa = Math.cos(ang);
            double sina = Math.sin(ang);
            t = x; 
            x = t*cosa + y*sina; 
            y = -t*sina + y*cosa;
        }
    }
    
    public _Vector2D C,S;
    public double ang;
    
    public RotatedRectangle(double x, double y, double width, double height, double angle) {
        C = new _Vector2D();
        S = new _Vector2D();
        C.x = x;
        C.y = y;
        S.x = width;
        S.y = height;
        ang = angle;
    }

    public static boolean RotRectsCollision(double x1, double y1, double width1, double height1, double angle1,
                                            double x2, double y2, double width2, double height2, double angle2) {
        return RotRectsCollision(new RotatedRectangle(x1,y1,width1,height1,angle1), 
                                 new RotatedRectangle(x2,y2,width2,height2,angle2));
    }
    
    // Rotated Rectangles Collision Detection, Oren Becker, 2001
    public static boolean RotRectsCollision(RotatedRectangle rr1, RotatedRectangle rr2)
    {
     _Vector2D A = new _Vector2D(), B = new _Vector2D(),   // vertices of the rotated rr2
               C,      // center of rr2
               BL, TR; // vertices of rr2 (bottom-left, top-right)

     double ang = rr1.ang - rr2.ang, // orientation of rotated rr1
           cosa = Math.cos(ang),           // precalculated trigonometic -
           sina = Math.sin(ang);           // - values for repeated use

     double t, x, a;      // temporary variables for various uses
     double dx;           // deltaX for linear equations
     double ext1, ext2;   // min/max vertical values

     // move rr2 to make rr1 cannonic
     C = new _Vector2D();
     C.x = rr2.C.x;
     C.y = rr2.C.y;
     C.sub(rr1.C);

     // rotate rr2 clockwise by rr2->ang to make rr2 axis-aligned
     C.rotate(rr2.ang);

     // calculate vertices of (moved and axis-aligned := 'ma') rr2
     BL = new _Vector2D();
     BL.x = C.x;
     BL.y = C.y;
     TR = new _Vector2D();
     TR.x = C.x;
     TR.y = C.y;
     BL.sub(rr2.S);
     TR.add(rr2.S);

     // calculate vertices of (rotated := 'r') rr1
     A.x = -rr1.S.y*sina; B.x = A.x; t = rr1.S.x*cosa; A.x += t; B.x -= t;
     A.y =  rr1.S.y*cosa; B.y = A.y; t = rr1.S.x*sina; A.y += t; B.y -= t;

     t = sina*cosa;

     // verify that A is vertical min/max, B is horizontal min/max
     if (t < 0)
     {
      t = A.x; A.x = B.x; B.x = t;
      t = A.y; A.y = B.y; B.y = t;
     }

     // verify that B is horizontal minimum (leftest-vertex)
     if (sina < 0) { B.x = -B.x; B.y = -B.y; }

     // if rr2(ma) isn't in the horizontal range of
     // colliding with rr1(r), collision is impossible
     if (B.x > TR.x || B.x > -BL.x) return false;

     // if rr1(r) is axis-aligned, vertical min/max are easy to get
     if (t == 0) {ext1 = A.y; ext2 = -ext1; }
     // else, find vertical min/max in the range [BL.x, TR.x]
     else
     {
      x = BL.x-A.x; a = TR.x-A.x;
      ext1 = A.y;
      // if the first vertical min/max isn't in (BL.x, TR.x), then
      // find the vertical min/max on BL.x or on TR.x
      if (a*x > 0)
      {
       dx = A.x;
       if (x < 0) { dx -= B.x; ext1 -= B.y; x = a; }
       else       { dx += B.x; ext1 += B.y; }
       ext1 *= x; ext1 /= dx; ext1 += A.y;
      }

      x = BL.x+A.x; a = TR.x+A.x;
      ext2 = -A.y;
      // if the second vertical min/max isn't in (BL.x, TR.x), then
      // find the local vertical min/max on BL.x or on TR.x
      if (a*x > 0)
      {
       dx = -A.x;
       if (x < 0) { dx -= B.x; ext2 -= B.y; x = a; }
       else       { dx += B.x; ext2 += B.y; }
       ext2 *= x; ext2 /= dx; ext2 -= A.y;
      }
     }

     // check whether rr2(ma) is in the vertical range of colliding with rr1(r)
     // (for the horizontal range of rr2)
     return !((ext1 < BL.y && ext2 < BL.y) ||
          (ext1 > TR.y && ext2 > TR.y));
    }    
}
