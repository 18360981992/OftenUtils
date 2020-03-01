package com.ifeng_tech.treasuryyitong.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ifeng_tech.treasuryyitong.R;
import com.ifeng_tech.treasuryyitong.appliction.DashApplication;
import com.ifeng_tech.treasuryyitong.view.dialog_jiazaikuang.Loading_view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mypc on 2018/4/27.
 */

public class MyUtils {

    private static Toast toast;
    public static void setToast(String ss){
        if(toast==null){
            toast=Toast.makeText(DashApplication.getAppContext(),ss,Toast.LENGTH_SHORT);
        }
        toast.setText(ss);
        toast.show();
    }

    /**
     * 禁止EditText输入空格
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText,int num){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);  // 表情
                Matcher emojiMatcher = emoji.matcher(source);

                String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if(emojiMatcher.find())  return "";  // 禁止输入表情

                if(matcher.find())return "";  // 禁止输入特殊字符

                if(source.equals(" "))return "";  // 禁止输入空格

                else return null;
            }

        };
        editText.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(num)}); // 设置最大输入长度
    }


    /**
     * 判断手机号是否符合规范
     * @param phoneNo 输入的手机号
     * @return
     */
    public static boolean isPhoneNumber(String phoneNo) {
        if(phoneNo.substring(0,1).equals("1")&&phoneNo.length()==11){
            return true;
        }else{
            return false;
        }
    }

    //邮箱验证
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断密码的格式 8-20位字母，数字组合密码
     * @param input
     * @return
     */
    public static boolean isPassWord(String input){
//        Pattern p = Pattern.compile("^[^ ]{8,20}$"); // 1.5.0 之前的密码判断
        Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(.{8,20})$");
        Matcher m = p.matcher(input);
        if(m.matches()){
            if(input.getBytes().length==input.length()){
                return true;
            }
        }
        return false;
    }



    // 判断是否符合身份证号码的规范
    public static boolean isIDCard(String IDCard) {
        if (IDCard != null) {
            String IDCardRegex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)";
            return IDCard.matches(IDCardRegex);
        }
        return false;
    }


    //字符串转指定格式时间  "yyyy-MM-dd" 到 "yyyy年MM月dd日"
    public static String getMyDate(String str) {
        return StringToDate(str, "yyyy-MM-dd", "yyyy年MM月dd日");
    }

    //字符串转指定格式时间   "yyyy年MM月dd日" 到 "yyyy-MM-dd"
    public static String getMyDate2(String str) {
        return StringToDate(str, "yyyy年MM月dd日", "yyyy-MM-dd");
    }

    // 时间转换
    public static String StringToDate(String dateStr, String dateFormatStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(dateFormatStr);
        Date date = null;
        try{
            date = sdf.parse(dateStr);
        } catch (Exception e){
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat(formatStr);

        return s.format(date);
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType){
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
            if (date == null) {
                return 0;
            } else {
                long currentTime = date.getTime(); // date类型转成long类型
                return currentTime;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     *
     * @param file  文件
     * @return
     */
    public static byte[] Filebyte(File file){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }


    /**
     * 检测
     * @return
     */
    public static String getSDPath(){
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("wc",
                    "SD card is not avaiable/writeable right now.");
            return null;
        }
        return sdStatus;

    }

    /**
     *  成功页面
     * @param context
     * @param dialog
     */
//    public static void setSubccedIntent(Intent intent, String title, int img, String text, String btn) {
//        intent.putExtra("title",title)
//                .putExtra("img", img)
//                .putExtra("text",text)
//                .putExtra("btn",btn);
//    }

    // 将自定义dialog 步骤 抽成方法 ====> 普通的dialog弹出  这样做的 好处 省去屏幕适配的代码适配
    public static void getPuTongDiaLog(Context context, Dialog dialog) {
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(params);
    }

    // 将共用的生成dialog步骤抽成方法 ====> 底部滑出
    public static void getDiaLogDiBu(Context context, Dialog dialog) {
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animation); // 添加动画
        dialog.show();
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(params);
    }

    /**
     *  登录页面的微弹窗
     * @param linearLayout
     * @param text
     * @param height
     * @param ss
     */
    public static void setObjectAnimator_login(final LinearLayout linearLayout, TextView text, int height, String ss){

        text.setText(ss);
        linearLayout.setVisibility(View.VISIBLE);
        ObjectAnimator animatorimg2 = ObjectAnimator.ofFloat(linearLayout, "translationY" ,0-height, 0);
        ObjectAnimator alphaimg2 = ObjectAnimator.ofFloat(linearLayout, "alpha", 0, 1.0f);
        AnimatorSet animatorSetimg2 = new AnimatorSet();
        animatorSetimg2.play(alphaimg2).with(animatorimg2);
        animatorimg2.setDuration(1000);
        animatorimg2.setInterpolator(new OvershootInterpolator(1));
        animatorimg2.start();

        animatorimg2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                DashApplication.getAppHanler().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            linearLayout.setVisibility(View.INVISIBLE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 登录微弹窗 有回调的
     * @param linearLayout
     * @param text
     * @param height
     * @param ss
     */
    public static void setObjectAnimator_login_huidiao(final LinearLayout linearLayout, TextView text, int height, String ss, final boolean flag){

        text.setText(ss);
        linearLayout.setVisibility(View.VISIBLE);
        ObjectAnimator animatorimg2 = ObjectAnimator.ofFloat(linearLayout, "translationY" ,0-height, 0);
        ObjectAnimator alphaimg2 = ObjectAnimator.ofFloat(linearLayout, "alpha", 0, 1.0f);
        AnimatorSet animatorSetimg2 = new AnimatorSet();
        animatorSetimg2.play(alphaimg2).with(animatorimg2);
        animatorimg2.setDuration(1000);
        animatorimg2.setInterpolator(new OvershootInterpolator(1));
        animatorimg2.start();

        animatorimg2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                DashApplication.getAppHanler().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            linearLayout.setVisibility(View.INVISIBLE);
                            if(flag){ // 成功的时候将当前页面销毁
                                myUtils_jieKou.chuan();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 转赠等页面专用的微弹窗   只消失微弹窗
     * @param linearLayout
     * @param img
     * @param text
     * @param height
     * @param flag
     * @param ss
     */
    public static void setObjectAnimator(final LinearLayout linearLayout, ImageView img, TextView text, int height, boolean flag, String ss){
        if(flag){// 成功
            img.setImageResource(R.drawable.chenggong_bai);
        }else{  //  失败
            img.setImageResource(R.drawable.shibai_bai);
        }
        text.setText(ss);
        linearLayout.setVisibility(View.VISIBLE);
        ObjectAnimator animatorimg2 = ObjectAnimator.ofFloat(linearLayout, "translationY" ,0-height, 0);
        ObjectAnimator alphaimg2 = ObjectAnimator.ofFloat(linearLayout, "alpha", 0, 1.0f);
        AnimatorSet animatorSetimg2 = new AnimatorSet();
        animatorSetimg2.play(alphaimg2).with(animatorimg2);
        animatorimg2.setDuration(1500);
        animatorimg2.setInterpolator(new OvershootInterpolator(1));
        animatorimg2.start();

        animatorimg2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                DashApplication.getAppHanler().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                            linearLayout.setVisibility(View.INVISIBLE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     *  安全保护中专用的微弹窗  2秒以后微弹窗消失，当前页面也消失
     * @param linearLayout
     * @param img
     * @param text
     * @param height
     * @param flag
     * @param ss
     */
    public static void setObjectAnimator_anquan(final LinearLayout linearLayout, ImageView img, TextView text, int height, final boolean flag, String ss){
        if(flag){// 成功
            img.setImageResource(R.drawable.chenggong_bai);
        }else{  //  失败
            img.setImageResource(R.drawable.shibai_bai);
        }
        text.setText(ss);
        linearLayout.setVisibility(View.VISIBLE);
        ObjectAnimator animatorimg2 = ObjectAnimator.ofFloat(linearLayout, "translationY" ,0-height, 0);
        ObjectAnimator alphaimg2 = ObjectAnimator.ofFloat(linearLayout, "alpha", 0, 1.0f);
        AnimatorSet animatorSetimg2 = new AnimatorSet();
        animatorSetimg2.play(alphaimg2).with(animatorimg2);
        animatorimg2.setDuration(1500);
        animatorimg2.setInterpolator(new OvershootInterpolator(1));
        animatorimg2.start();

        animatorimg2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                DashApplication.getAppHanler().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                            linearLayout.setVisibility(View.INVISIBLE);
                            if(flag){ // 成功的时候将当前页面销毁
                                myUtils_jieKou.chuan();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public interface MyUtils_JieKou{
        void chuan();
    }
    public static MyUtils_JieKou myUtils_jieKou;

    public static void setMyUtils_jieKou(MyUtils_JieKou myUtils_jieKou) {
        MyUtils.myUtils_jieKou = myUtils_jieKou;
    }

    /**
     *  计算 下一年的时间
     * @param date
     * @return
     */
    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, +1);//+1今天的时间加一年
        date = calendar.getTime();
        return date;
    }

    /**
     *  网络加载进度框
     * @param context
     * @param text
     * @return
     */
    public static ProgressDialog getProgressDialog(Context context, String text){
        //  进度框
//        ProgressDialog aniDialog = new ProgressDialog(context);
//        aniDialog.setCancelable(false);  // 设置android4.0以后 dialog弹出后会点击屏幕或物理返回键，dialog不消失
//        aniDialog.setCanceledOnTouchOutside(false); // dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
//        aniDialog.setMessage(text);
//        aniDialog.show();
        Loading_view aniDialog = new Loading_view(context, R.style.CustomDialog);
        aniDialog.show();
        return aniDialog;
    }

    /**
     * 根据当前的状态来旋转箭头。
     */
    public static void rotateArrow(ImageView arrow, boolean flag) {
        float pivotX = arrow.getWidth() / 2f;
        float pivotY = arrow.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        // flag为true则向上
        if (flag) {
            fromDegrees = 0f;
            toDegrees = 180f;
        } else {
            fromDegrees = 180f;
            toDegrees = 0f;
        }
        //旋转动画效果   参数值 旋转的开始角度  旋转的结束角度  pivotX x轴伸缩值
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees,
                pivotX, pivotY);
        //该方法用于设置动画的持续时间，以毫秒为单位
        animation.setDuration(100);
        //设置重复次数
        //animation.setRepeatCount(int repeatCount);
        //动画终止时停留在最后一帧
        animation.setFillAfter(true);
        //启动动画
        arrow.startAnimation(animation);
    }


    /**
     * <pre>
     * 根据指定的日期字符串获取星期几
     * </pre>
     *
     * @param strDate 指定的日期字符串(yyyy-MM-dd 或 yyyy/MM/dd)
     * @return week
     *         星期几(MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY)
     */
    public static String getWeekByDateStr(String strDate){
        int year = Integer.parseInt(strDate.substring(0, 4));
        int month = Integer.parseInt(strDate.substring(5, 7));
        int day = Integer.parseInt(strDate.substring(8, 10));

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);

        String week = "";
        int weekIndex = c.get(Calendar.DAY_OF_WEEK);

        switch (weekIndex)
        {
            case 1:
                week = "SUNDAY";
                break;
            case 2:
                week = "MONDAY";
                break;
            case 3:
                week = "TUESDAY";
                break;
            case 4:
                week = "WEDNESDAY";
                break;
            case 5:
                week = "THURSDAY";
                break;
            case 6:
                week = "FRIDAY";
                break;
            case 7:
                week = "SATURDAY";
                break;
        }
        return week;
    }


    // 获取拼接的email
    public static String getEmail(String email){
        String tou = email.substring(0, 2);
        String wei = email.substring(email.length()-10, email.length());
        int i=email.length()-12;  // 获取“*”的个数
        String ss="";
        for (int j=0;j<i;j++){
            ss=ss+"*";
        }
        String newEmail=tou+ss+wei;
        return newEmail;
    }


    /**
     * 判断服务是否开启
     *
     * @param mContext
     * @param className 这里是包名+类名 xxx.xxx.xxx.TestService
     * @return
     */
    public static boolean isServiceRunning(Context mContext,String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size()>0)) {
            return isRunning;
        }

        for (int i=0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }

        return isRunning;
    }


    /**
     * 判断 几年前 几月前 几天前 几小时前 几分钟前
     */
    private static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static String friendlyTimeFormat(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        //判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        int curYear = cal.get(Calendar.YEAR);
        int curMonth = cal.get(Calendar.MONTH) + 1;
        int curDay = cal.get(Calendar.DATE);

        Calendar c = Calendar.getInstance();
        c.setTime(time);
        int date_year = c.get(Calendar.YEAR);
        int date_month = c.get(Calendar.MONTH)+1;
        int date_day = c.get(Calendar.DATE);

        long ct = cal.getTime().getTime() / 86400000 ; // 当前
        long lt = time.getTime() / 86400000;  // 后台数据
        int days = (int) (ct - lt);
        if(days>0&&days<=30){
            if(curDay!=date_day){
                ftime=(curDay-date_day)+"天前";
                if((curDay-date_day)<0){
                    ftime=(curDay-date_day+30)+"天前";
                }
                return ftime;
            }
        }else if (days > 30 && days <= 60) {
            ftime = "1个月前";return ftime;
        } else if (days > 60 && days <= 90) {
            ftime = "2个月前";return ftime;
        } else if (days > 90 && days <= 120) {
            ftime = "3个月前";return ftime;
        } else if (days > 120 && days <= 150) {
            ftime = "4个月前";return ftime;
        } else if (days > 150 && days <= 180) {
            ftime = "5个月前";return ftime;
        } else if (days > 180 && days <= 210) {
            ftime = "6个月前";return ftime;
        } else if (days > 210 && days <= 240) {
            ftime = "7个月前";return ftime;
        } else if (days > 240 && days <= 270) {
            ftime = "8个月前";return ftime;
        } else if (days > 270 && days <= 300) {
            ftime = "9个月前";return ftime;
        } else if (days > 300 && days <= 330) {
            ftime = "10个月前";return ftime;
        } else if (days > 330 && days <= 365) {
            ftime = "11个月前";return ftime;
        } else if (days > 365 && days <= 730) {
            ftime = "1年前";return ftime;
        } else if (days > 730 && days <= 1095) {
            ftime = "2年前";return ftime;
        } else if (days > 1095) {
            int i = days / 365;
            ftime=i+"年前";return ftime;
        }
//        if(curYear!=date_year){
//            ftime=(curYear-date_year)+"年前";
//            return ftime;
//        }
//
//        if(curMonth!=date_month){
//            ftime=(curMonth-date_month)+"月前";
//            return ftime;
//        }
//        if(curDay!=date_day){
//            ftime=(curDay-date_day)+"天前";
//            return ftime;
//        }
        return ftime;
    }


    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    private static Date toDate(String sdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 提供精确的加法运算。
     *
     * @param v1
     *            被加数
     * @param v2
     *            加数
     * @return 两个参数的和
     */

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1
     *            被乘数
     * @param v2
     *            乘数
     * @return 两个参数的积
     */

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

}
