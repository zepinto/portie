package pt.lsts.syslist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import pt.lsts.imc.Announce;
import pt.lsts.imc.VehicleState;
import pt.lsts.portie.R;

/**
 * Created by zp on 29-05-2016.
 */
public class SystemIconView extends View {

    private Announce.SYS_TYPE type = Announce.SYS_TYPE.CCU;
    private VehicleState.OP_MODE opmode = VehicleState.OP_MODE.BOOT;
    private Paint forePaint;

    public SystemIconView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public SystemIconView(Context context, AttributeSet attr, int c) {
        super(context, attr, c);
        init();
    }

    public SystemIconView(Context context) {
        super(context);
        init();
    }

    private void init() {
        forePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        forePaint.setColor(0xffffffff);
        forePaint.setTextSize(14);
    }

    public void setOpmode(VehicleState.OP_MODE opmode) {
        this.opmode = opmode;

        switch (opmode) {
            case SERVICE:
                setBackgroundColor(getResources().getColor(R.color.colorService));
                break;
            case CALIBRATION:
                setBackgroundColor(getResources().getColor(R.color.colorBoot));
                break;
            case ERROR:
                setBackgroundColor(getResources().getColor(R.color.colorError));
                break;
            case MANEUVER:
                setBackgroundColor(getResources().getColor(R.color.colorManeuver));
                break;
            case EXTERNAL:
                setBackgroundColor(getResources().getColor(R.color.colorOther));
                break;
            case BOOT:
                setBackgroundColor(getResources().getColor(R.color.colorBoot));
                break;
            default:
                setBackgroundColor(getResources().getColor(R.color.colorOther));
                break;
        }
    }

    public void setType(Announce.SYS_TYPE type) {
        this.type = type;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int posx = 0;
        int posy = 0;
        switch (type) {
            case CCU:
                canvas.drawText("CCU", posx, posy, forePaint);
                break;
            case HUMANSENSOR:
                canvas.drawText("H", posx, posy, forePaint);
                break;
            case UUV:
                canvas.drawText("AUV", posx, posy, forePaint);
                break;
            case USV:
                canvas.drawText("ASV", posx, posy, forePaint);
                break;
            case UAV:
                canvas.drawText("UAV", posx, posy, forePaint);
                break;
            case UGV:
                canvas.drawText("UGV", posx, posy, forePaint);
                break;
            case STATICSENSOR:
                canvas.drawText("S", posx, posy, forePaint);
                break;
            case MOBILESENSOR:
                canvas.drawText("M", posx, posy, forePaint);
                break;
            case WSN:
                canvas.drawText("WSN", posx, posy, forePaint);
                break;
        }

    }
}
