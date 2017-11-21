package lab_1.pwta.kgit.pg.lab_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

;import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by student on 18.11.2017.
 */

public class CustomView extends View implements Serializable {
    public CustomView(Context context) {
        super(context);
    }

    List<point> points = new ArrayList<>();

    // if true then moving active point
    boolean moving_point = false;
    // index in points list of moving point
    int acticvePointMove = 0;

    int width = 0;
    int height = 0;

    // polynomial values
    double a = 0;
    double b = 0;
    double c = 0;

    // miejsca zerowe i wierzchołek paraboli
    double x0;
    double x0_y;
    double x1;
    double x1_y;
    double w1;
    double w2;

    // czy występuje miejsce zerowe
    boolean miejsca_zerowe = true;

    // pozycja w tablicy punktów miejsc zerwocyh i wierzchołka
    int wlocation = 0;
    int x0_location = 0;
    int x1_location = 0;


    // scale
    int scale = 8;


    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPolynomialValues(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        points.clear();

        for (double i = -100.0; i < 100.0; i += 1) {
            double y = calculateValue(i);
            point point = new point();
            point.x = getX(i);
            point.y = getY(y);

            if (point.x < width && point.y < height) {
                points.add(point);
            }
        }

        point wp = new point();
        wp.x = getX(w1);
        wp.y = getY(w2);
        points.add(wp);

        //dodanie miejsc zerowych jeśli występują
        point x0p = new point();
        point x1p = new point();
        if (miejsca_zerowe) {
            x0p.x = getX(x0);
            x0p.y = getY(x0_y);
            points.add(x0p);

            x1p.x = getX(x1);
            x1p.y = getY(x1_y);
            points.add(x1p);
        }

        // posortowanie punktów według wartości X
        Collections.sort(points, new Comparator<point>() {
            @Override
            public int compare(point p1, point p2) {
                return ((Integer) p1.x).compareTo(p2.x);
            }
        });

        // przypisanie pozycji w liscie punktom paraboli (miejsca zerowe, wierzchołek
        int idx = 0;
        for (point p : points) {
            if (p.x == wp.x && p.y == wp.y) {
                wlocation = idx;
            }
            if (miejsca_zerowe) {
                if (p.x == x0p.x && p.y == x0p.y) {
                    x0_location = idx;
                } else if (p.x == x1p.x && p.y == x1p.y) {
                    x1_location = idx;
                }
            }

            idx++;
        }

        invalidate();
    }

    //ustawienie wartości paraboli
    public void drawParabola(double x0, double x1, double w1, double w2) {
        points.clear();
        System.out.println("draw parabola");
        this.x0 = x0;
        this.x0_y = 0;
        this.x1 = x1;
        this.x1_y = 0;
        this.w1 = w1;
        this.w2 = w2;

        if (Double.isNaN(x0) || Double.isNaN(x1)) {
            miejsca_zerowe = false;
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("down");
                if ((Math.abs((int) event.getX() - points.get(wlocation).x) < 20) && (Math.abs((int) event.getY() - points.get(wlocation).y) < 20)) {
                    moving_point = true;
                    acticvePointMove = wlocation;
                }
                if (miejsca_zerowe) {
                    if ((Math.abs((int) event.getX() - points.get(x0_location).x) < 20) && (Math.abs((int) event.getY() - points.get(x0_location).y) < 20)) {
                        if (!moving_point) {
                            moving_point = true;
                            acticvePointMove = x0_location;
                        }

                    }
                    if ((Math.abs((int) event.getX() - points.get(x1_location).x) < 20) && (Math.abs((int) event.getY() - points.get(x1_location).y) < 20)) {
                        if (!moving_point) {
                            moving_point = true;
                            acticvePointMove = x1_location;
                        }
                    }
                }
                break;
            case ACTION_UP:
                moving_point = false;
                break;
            case MotionEvent.ACTION_MOVE:
                //przesuwanie aktywnego punktu
                if (moving_point) {
                    points.get(acticvePointMove).x = (int) event.getX();
                    points.get(acticvePointMove).y = (int) event.getY();
                    invalidate();
                }
                break;
        }

        // jesli przesuwamy punkt to zatrzymujemy obsługę , w rpzeciwnym razie przekazujemy dalej
        if (moving_point) {
            return true;
        } else {
            return false;
        }

    }

    // rysowanie układu współrzędnych
    public void drawCoordinateSystem(Canvas canvas) {
        Paint p = new Paint();
        p.setARGB(255, 255, 0, 0);
        p.setStrokeWidth(2);
        canvas.drawLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight(), p);

        canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2, p);
        p.setARGB(255, 0, 255, 0);

        p.setTextSize(28);
        p.setColor(Color.GRAY);
        for (int i = -100; i < 100; i += 10) {
            canvas.drawText(String.valueOf(i), canvas.getWidth() / 2 + (i * scale), canvas.getHeight() / 2, p);
        }

        p.setTextSize(28);
        p.setColor(Color.GRAY);
        for (int i = -100; i < 100; i += 10) {
            canvas.drawText(String.valueOf(i), canvas.getWidth() / 2, canvas.getHeight() / 2 - (i * scale), p);
        }

        p.setTextSize(48);

        canvas.drawText("X", canvas.getWidth() - 40, canvas.getHeight() / 2 + 50, p);

        canvas.drawText("Y", canvas.getWidth() / 2 + 40, 50, p);

    }

    @Override
    public void draw(Canvas c) {
        System.out.println("draw");
        super.draw(c);

        drawCoordinateSystem(c);

        Paint p = new Paint();
        p.setARGB(255, 0, 255, 0);

        p.setColor(Color.BLUE);
        p.setStrokeWidth(5);


        point lastPoint = null;
        for (point point : points) {
            if (lastPoint != null) {
                c.drawLine(lastPoint.x, lastPoint.y, point.x, point.y, p);
            }
            drawPoint(point.x, point.y, c, p);
            lastPoint = point;
        }


        p.setColor(Color.RED);
        p.setStrokeWidth(20);

        if (miejsca_zerowe) {
            drawPoint(points.get(x0_location).x, points.get(x0_location).y, c, p);
            drawPoint(points.get(x1_location).x, points.get(x1_location).y, c, p);
        }


        drawPoint(points.get(wlocation).x, points.get(wlocation).y, c, p);


    }


    private double calculateValue(double x) {
        //ax^2 + bx + c = 0;
        double result = (a * (x * x)) + (b * x) + c;
        return result;
    }

    private void drawPoint(int x, int y, Canvas c, Paint p) {
        c.drawPoint(x, y, p);
    }

    public int getX(double x) {
        return (int) (width / 2 + ((float) x * scale));
    }

    public int getY(double y) {
        return (int) (height / 2 - ((float) y * scale));
    }

    class point {
        int x;
        int y;
    }
}

