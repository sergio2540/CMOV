package my.game.achmed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

@SuppressLint("NewApi")
public class DogeView extends SurfaceView implements Callback{
	Options opt;
	Bitmap image;
	Bitmap doge;
	public static float x = 0;
	public static float y = 0;


	public DogeView(Context context) {
		super(context);
		initializeImages();
		getHolder().addCallback(this);
		// TODO Auto-generated constructor stub
	}
	public DogeView(Context context, AttributeSet attrs){
		super(context, attrs);//comentar e ver erro
		initializeImages();
		getHolder().addCallback(this);


	}
	
	public DogeView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context,attrs,defStyleAttr);
		initializeImages();
		getHolder().addCallback(this);


	}
	
	 
	
	
	
	private void initializeImages(){
		opt = new BitmapFactory.Options();
		opt.inMutable = true;
		image = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper,opt);
		doge = BitmapFactory.decodeResource(getResources(), R.drawable.doge,opt);
	}

	@Override
	public void onDraw (Canvas canvas){
		//canvas.setBitmap(image);
		//canvas.drawColor(Color.BLUE);
		canvas.drawBitmap(doge, x, y, null);

		
	}
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		Canvas canvas = arg0.lockCanvas();
		//canvas.drawColor(Color.MAGENTA);
		arg0.unlockCanvasAndPost(canvas);

		
		
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		setBackgroundResource(R.drawable.wallpaper);
		Canvas canvas = arg0.lockCanvas();
		//canvas.drawColor(Color.BLACK);
		//canvas.drawBitmap(image);
		canvas.drawBitmap(doge, x, y, null);
		arg0.unlockCanvasAndPost(canvas);
		setWillNotDraw(false);
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
