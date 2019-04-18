package com.thr3.chhotu;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    CameraView cameraView;
    ImageView click,logo,menu_pic,gallery,edit_text,speak_pic;
    RelativeLayout splash,splash_cover,logo_div,f1,f2,f3,f4,f1_ico,f2_ico,f3_ico,f4_ico,permission_camera,editor;
    boolean camStarted=false,isMenuOpen=false,sum=false;
    Button allow_camera;
    TextView start;
    ProgressBar load,click_load;
    String atricle="";
    EditText article_text;
    CardView edit,menu,shortify,open_camera,voice,clear,speak;
    TextToSpeech textToSpeech;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences("settings", 0);
        cameraView=findViewById(R.id.cam);
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {}
            @Override
            public void onError(CameraKitError cameraKitError) {}
            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bmp = cameraKitImage.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                openEditor(ocrGetString(bitmap));
                click_load.setVisibility(View.GONE);click.setEnabled(true);
            }
            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) { }
        });
        click =findViewById(R.id.click) ;
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_load.setVisibility(View.VISIBLE);click.setEnabled(false);
                cameraView.captureImage();
            }
        });
        splash_cover=findViewById(R.id.splash_cover);
        logo= findViewById(R.id.logo);
        splash=findViewById(R.id.splash);
        f1=findViewById(R.id.f1);
        f2=findViewById(R.id.f2);
        f3=findViewById(R.id.f3);
        f4=findViewById(R.id.f4);
        f1_ico= findViewById(R.id.f1_ico);
        f2_ico= findViewById(R.id.f2_ico);
        f3_ico= findViewById(R.id.f3_ico);
        f4_ico= findViewById(R.id.f4_ico);
        load=findViewById(R.id.load);
        click_load=findViewById(R.id.click_load);
        logo_div= findViewById(R.id.logo_div);

        textToSpeech = new TextToSpeech(this, this);
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override public void onStart(String s) {speak_pic.setImageResource(R.drawable.dont_speak);}
            @Override public void onDone(String s) {speak_pic.setImageResource(R.drawable.speak);}
            @Override public void onError(String s) {
            }
        });

        gallery= findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
            }
        });
        edit_text= findViewById(R.id.edit_text);
        edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditor("");
            }
        });
        article_text=findViewById(R.id.article_text);

        clear=findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                article_text.setText("");
            }
        });

        speak_pic=findViewById(R.id.speak_pic) ;
        speak=findViewById(R.id.speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textToSpeech.isSpeaking()){
                    textToSpeech.speak(article_text.getText(),TextToSpeech.QUEUE_FLUSH,null,"1");
                    speak_pic.setImageResource(R.drawable.dont_speak);}
                else{textToSpeech.stop();speak_pic.setImageResource(R.drawable.speak);}
            }
        });
        shortify=findViewById(R.id.shortify);
        shortify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleY(edit.getHeight()+(int)dptopx(80),130,edit);
                isMenuOpen=false;menu_pic.setImageResource(R.drawable.up);
                atricle=article_text.getText().toString();
                article_text.setText((new Summarizer()).Summarize(article_text.getText().toString()));
                clear.setVisibility(View.INVISIBLE);speak.setVisibility(View.INVISIBLE);
                menu_pic.setImageResource(R.drawable.again);
                if(!sum) { sum=true; }
            }
        });
        voice=findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                startActivityForResult(intent, 1);
            }
        });
        open_camera=findViewById(R.id.open_camera);
        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleY(edit.getHeight()+(int)dptopx(80),130,edit);isMenuOpen=false;menu_pic.setImageResource(R.drawable.up);
                new Handler().postDelayed(new Runnable() {@Override public void run()
                {
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.editor_exit);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override public void onAnimationStart(Animation animation) {}
                        @Override public void onAnimationRepeat(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            editor.setVisibility(View.GONE);
                            new Handler().postDelayed(new Runnable() {@Override public void run()
                            {
                                click.setVisibility(View.VISIBLE);
                                click.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.click_grow));
                            }},500);
                            if(!camStarted){cameraView.start();camStarted=true;}
                            cameraView.setVisibility(View.VISIBLE);
                        }
                    });
                    edit.startAnimation(anim);
                }},150);
            }
        });

        menu_pic= findViewById(R.id.menu_pic);
        menu= findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sum)
                {
                    if(!isMenuOpen)
                    {
                        if(camStarted){cameraView.stop();camStarted=false;}cameraView.setVisibility(View.GONE);
                        scaleY(edit.getHeight()-(int)dptopx(80),130,edit);
                        isMenuOpen=true;
                        menu_pic.setImageResource(R.drawable.down);
                    }
                    else
                    {
                        scaleY(edit.getHeight()+(int)dptopx(80),130,edit);isMenuOpen=false;
                        menu_pic.setImageResource(R.drawable.up);
                    }
                }
                else
                {
                    clear.setVisibility(View.VISIBLE);speak.setVisibility(View.VISIBLE);
                    menu_pic.setImageResource(R.drawable.up);
                    article_text.setText(atricle);
                    sum=false;
                }
            }
        });

        permission_camera= findViewById(R.id.permission_camera);
        allow_camera =findViewById(R.id.allow_camera);
        allow_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, 1);
            }
        });

        start= findViewById(R.id.start);
        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        start.setBackgroundResource(R.drawable.button_touch);start.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case MotionEvent.ACTION_UP:
                        start.setBackgroundResource(R.drawable.button);start.setTextColor(getResources().getColor(R.color.colorAccent));
                        closeIntro();
                        break;
                }
                return true;
            }
        });
        editor=findViewById(R.id.editor);editor.setVisibility(View.INVISIBLE);
        splash();
    }
    public void splash()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_trans);
                splash_cover.setVisibility(View.GONE);
                Animation anima = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_reveal);
                anima.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation)
                    {
                        if (settings.getBoolean("first run", true))
                        {
                            new Handler().postDelayed(new Runnable() {@Override public void run()
                            {
                                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.f_grow);
                                f1_ico.setVisibility(View.VISIBLE);f1_ico.startAnimation(anim);
                            }},200);
                            new Handler().postDelayed(new Runnable() {@Override public void run()
                            {
                                f1.setVisibility(View.VISIBLE);scaleX(splash.getWidth()-(int)dptopx(20),300,f1);
                            }},500);
                            new Handler().postDelayed(new Runnable() {@Override public void run()
                            {
                                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.f_grow);
                                f2_ico.setVisibility(View.VISIBLE);f2_ico.startAnimation(anim);
                            }},800);
                            new Handler().postDelayed(new Runnable() {@Override public void run()
                            {
                                f2.setVisibility(View.VISIBLE);scaleX(splash.getWidth()-(int)dptopx(20),300,f2);
                            }},1100);
                            new Handler().postDelayed(new Runnable() {@Override public void run()
                            {
                                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.f_grow);
                                f3_ico.setVisibility(View.VISIBLE);f3_ico.startAnimation(anim);
                            }},1400);
                            new Handler().postDelayed(new Runnable() {@Override public void run()
                            {
                                f3.setVisibility(View.VISIBLE);scaleX(splash.getWidth()-(int)dptopx(20),300,f3);
                            }},1700);
                            new Handler().postDelayed(new Runnable() {@Override public void run()
                            {
                                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.f_grow);
                                f4_ico.setVisibility(View.VISIBLE);f4_ico.startAnimation(anim);
                            }},2000);
                            new Handler().postDelayed(new Runnable() {@Override public void run()
                            {
                                f4.setVisibility(View.VISIBLE);scaleX(splash.getWidth()-(int)dptopx(20),300,f4);
                            }},2300);
                            new Handler().postDelayed(new Runnable() {@Override public void run()
                            {
                                load.setVisibility(View.GONE);start.setVisibility(View.VISIBLE);
                                scaleX(splash.getWidth()/3,100,start);
                                new Handler().postDelayed(new Runnable()
                                {@Override public void run() {start.setText("Get Started");}},300);
                            }},3000);
                            settings.edit().putBoolean("first run", false).apply();
                        }
                        else
                        {
                            closeIntro();
                        }
                    }
                });
                logo_div.setVisibility(View.VISIBLE);logo_div.startAnimation(anima);logo.startAnimation(anim);
            }
        },1500);
    }
    @Override
    public void onInit(int Text2SpeechCurrentStatus)
    {
        textToSpeech.setLanguage(Locale.US);
    }
    public void openEditor(final String text)
    {
        article_text.setMinimumHeight(editor.getHeight()-(int)dptopx(55));
        article_text.setText(text);cameraView.setVisibility(View.GONE);click.setVisibility(View.GONE);
        edit=findViewById(R.id.edit);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.editor_enter);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation) {if(camStarted){cameraView.stop();camStarted=false;}}
            @Override public void onAnimationRepeat(Animation animation) {}
        });
        editor.setVisibility(View.VISIBLE);edit.startAnimation(anim);
    }
    public void closeIntro()
    {
        permission_camera.setVisibility(View.VISIBLE);
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {permission_camera.setVisibility(View.GONE);if(!camStarted){cameraView.start();camStarted=true;}}
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_exit_ico);
        Animation anima = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_exit);
        anima.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation)
            {
                splash.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {@Override public void run()
                {
                    Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.click_grow);
                    click.setVisibility(View.VISIBLE);click.startAnimation(anim);
                }},500);
            }
        });
        logo_div.startAnimation(anima);logo.startAnimation(anim);
    }
    public void scaleX(int x,int time,final View view)
    {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(),x);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = (Integer) valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(time);anim.setInterpolator(new AccelerateDecelerateInterpolator());anim.start();
    }
    public void scaleY(int y,int time,final View view)
    {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(),y);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(time);anim.start();
    }
    public String ocrGetString(Bitmap bitmap)
    {
        String text="";
        try {
            if (bitmap != null) {
                TextRecognizer textRecognizer = new TextRecognizer.Builder(MainActivity.this).build();
                if (!textRecognizer.isOperational()) {
                    IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                    boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

                    if (hasLowStorage) {
                        Toast.makeText(MainActivity.this, "Low Storage", Toast.LENGTH_LONG).show();
                    }
                }
                Frame imageFrame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
                for (int i = 0; i < textBlocks.size(); i++) {
                    TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                    if(i<textBlocks.size()){text=text+textBlock.getValue()+"\n";}
                    else{text=text+textBlock.getValue();}
                }
                return text;
            }
        } catch (Exception e) {Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();}
        return "";
    }
    public float dptopx(float num)
    {return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, num, getResources().getDisplayMetrics());}
    public float pxtodp(float num)
    {return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, num, getResources().getDisplayMetrics());}
    @Override
    protected void onActivityResult(int requestCode, int resultcode, Intent intent) {
        super.onActivityResult(requestCode, resultcode, intent);
        ArrayList<String> speech;
        if (resultcode == RESULT_OK && requestCode == 1) {
            speech = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(article_text.getText().length()>0) {article_text.setText(article_text.getText()+"\n"+speech.get(0));}
            else {article_text.setText(speech.get(0));}
            article_text.setSelection(article_text.getText().length());
        }
        if (resultcode == RESULT_OK && requestCode == 2) {
            try
            {
                Uri imageUri = intent.getData();
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), imageUri);
                openEditor(ocrGetString(bitmap));
            }
            catch(Exception e){}
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {permission_camera.setVisibility(View.GONE);}
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {if(!camStarted){cameraView.start();camStarted=true;}}
    }

    @Override
    protected void onPause() {
        if(camStarted){cameraView.stop();camStarted=false;}
        if(textToSpeech.isSpeaking()){textToSpeech.stop();speak_pic.setImageResource(R.drawable.speak);}
        super.onPause();
    }
}