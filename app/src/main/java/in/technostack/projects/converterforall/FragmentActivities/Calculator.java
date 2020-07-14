package in.technostack.projects.converterforall.FragmentActivities;

import android.app.Fragment;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.transition.Fade;
import androidx.transition.Transition;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.mariuszgromada.math.mxparser.Expression;
import java.text.DecimalFormat;
import in.technostack.projects.converterforall.MainActivity;
import in.technostack.projects.converterforall.R;


public class Calculator extends Fragment {

    View calculatorView;
    LinearLayout standard;
    ScrollView history;
    TextView calculation, answer;
    String sCalculation = "", sExpression="", sAnswer = "", sHistory="", current_operator="", current_number="", prevNumber="";
    Button  clr;
    DecimalFormat df;
    Transition bounds;
    Button  zero;
    Button  one;
    Button  two;
    Button  three;
    Button  four;
    Button  five;
    Button  six;
    Button  seven;
    Button  eight;
    Button  nine;
    Button  per;
    Button  div;
    Button  mul;
    Button  minus;
    Button  plus;
    Button  equ;
    Button  dot;
    Button  del;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        calculatorView=inflater.inflate(R.layout.calculator,container,false);
        calculation = calculatorView.findViewById(R.id.calculation);
        history=calculatorView.findViewById(R.id.historyScrollbar);
        ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.calci));
        ((MainActivity) getActivity()).setScreenName(getResources().getString(R.string.calci));
        //initialize answer
        answer = calculatorView.findViewById(R.id.answer);
        df= new DecimalFormat("#.###########");
        setListeners();
        bounds=new Fade();
        standard=calculatorView.findViewById(R.id.standard);
        setClearBtnText();
        //((MainActivity)getActivity()).canChangeOrientation(false);
        return  calculatorView;
    }

    public void onClickNumber(View v) {
        //we need to find which button is pressed
        Button bn = (Button) v;
        boolean noModulus=true;
        try{
            if (sCalculation.charAt(sCalculation.length()-2)=='%')
                noModulus=false;
        }
        catch (Exception e){
            noModulus=true;
        }
        try {
            if(current_operator=="")
            {
                if (current_number.length()<16){
                    if (noModulus){
                        current_number+=bn.getText();
                        sCalculation += bn.getText();
                    }
                }
            }
            else {
                if (noModulus){
                    current_operator="";
                    current_number+=bn.getText();
                    sCalculation += bn.getText();
                }
            }
            sExpression=sCalculation;
            updateCalculation();
        }
        catch (Exception e){

        }

    }

    public void onClickOperator(View v) {
        Button ob = (Button) v;
        //if sAnswer is null means no calculation needed
        try {
            if (sCalculation.length()!=0 && getcharfromLast(sCalculation, 1)!='.'){
                if (sAnswer.length() != 0) {
                    //we check last char is operator or not
                    if (current_operator.length() != 0) {
                        char c = getcharfromLast(sCalculation, 2);// 2 is the char from last because our las char
                        if (c == '+' || c == '-' || c == 'x' || c == '/') {
                            sCalculation = sCalculation.substring(0, sCalculation.length() - 3);
                            sExpression=sCalculation;
                        }
                    }
                    sCalculation = sCalculation + "\n" + ob.getText() + " ";
                    current_number="";
                    current_operator = ob.getText().toString();
                    updateCalculation();
                    sExpression=sCalculation;
                }
            }
            else if (sCalculation.length()==0 && sAnswer.length() != 0){
                sCalculation=sAnswer+ "\n" + ob.getText() + " ";
                current_operator = ob.getText().toString();
                sExpression=sCalculation;
                calculation.setText(sHistory+sCalculation);
                scrollDown();
            }
        }
        catch (Exception e){

        }


    }

    private char getcharfromLast(String s, int i) {
        char c = s.charAt(s.length() - i);
        return c;

    }

    public void onClickClear(View v) {
        Button b=(Button)v;
        if (b.getText().toString().equalsIgnoreCase("C")){
            sCalculation="";
            sExpression="";
            sAnswer="";
            current_number="";
            current_operator="";
            calculation.setText(sHistory);
            answer.setText(sAnswer);
            setClearBtnText();
        }
        else {
            sCalculation="";
            sExpression="";
            sAnswer="";
            current_number="";
            current_operator="";
            sHistory="";
            calculation.setText(sHistory);
            answer.setText(sAnswer);
            setClearBtnText();
        }
    }

    public void updateCalculation() {
        String exp = sExpression.replaceAll("\\s+", "");
        String expression = exp.replaceAll("x", "*");

        try {
            // Evaluate an expression
            Expression e1= new Expression(expression.replace(",",""));
            double res=e1.calculate();
            //String result=Double.toString(res);
            String result=df.format(res);
            // Calculate the result and display
            calculation.setText(sHistory+""+sCalculation);
            scrollDown();
            if(result.equals("NaN"))
            {
                if(sAnswer.equals(""))
                {
                    sAnswer="";
                    answer.setText("");
                    answer.setHint("0");
                }
                else
                {
                    sAnswer="∞";
                    answer.setText(sAnswer);
                }
            }
            else
            {
                sAnswer=result;
                answer.setText(result);
            }

        } catch (ArithmeticException ex) {
            // Display an error message
            answer.setText("Error");
        }
        setClearBtnText();

    }

    public void onDotClick(View view) {
        //create boolean dot_present check if dot is present or not.
        try{
            if(current_number.length()!=0){
                if (!current_number.contains(".")){
                    current_number+=".";
                    sCalculation += ".";
                    sExpression=sCalculation;
                }
            }
            else{
                current_number="0.";
                sCalculation += "0.";
                sExpression=sCalculation;
                if (sAnswer.length()==0){
                    sAnswer=current_number;
                    answer.setText(sAnswer);
                }
            }
            calculation.setText(sHistory+sCalculation);
            scrollDown();
        }
        catch (Exception e){

        }

    }

    public void onModuloClick(View view) {
        try {
            if (sCalculation.length()!=0 && getcharfromLast(sCalculation, 1) != '.') {
                if (sAnswer.length()!=0 && getcharfromLast(sCalculation, 1) != ' ') {
                    String[] str=PercentOrModulo();
                    if (str.length==2){
                        int position=Integer.parseInt(str[0]);
                        Expression e = new Expression(sExpression.substring(0,position).replace("x","*"));
                        double ans=e.calculate();
                        double percent=Double.parseDouble(str[1]);
                        percent = ((ans*percent)/100);
                        current_number=df.format(percent);
                        sCalculation=sCalculation.substring(0,position+3)+df.format(percent);
                    }
                    else if (str.length==1){
                        int position=Integer.parseInt(str[0]);
                        String value=sCalculation.substring(position);
                        Expression e = new Expression(value+"%");
                        double ans=e.calculate();
                        current_number=df.format(ans);
                        sCalculation = sCalculation.substring(0,position)+df.format(ans);
                    }
                    else {
                        Expression e = new Expression(sCalculation+"%");
                        double ans=e.calculate();
                        current_number=df.format(ans);
                        sCalculation = df.format(ans);
                    }
                    sExpression = sCalculation;
                    updateCalculation();
                }
            }
            else if (sCalculation.length()==0 && sAnswer.length()!=0){
                Expression e=new Expression(sAnswer+"%");
                double ans=e.calculate();
                sCalculation=df.format(ans);
                sExpression=sCalculation;
                updateCalculation();
            }
        }
        catch (Exception e){

        }

    }

    public void onClickEqual(View view) {
        showresult();
    }

    public void showresult() {
        try {
            if (sAnswer.length() != 0 && sCalculation.length()!=0) {
                TextPaint paint=answer.getPaint();
                int wordWidth=(int) paint.measureText("a",0,1);
                int screenWidth=getResources().getDisplayMetrics().widthPixels;
                int num=screenWidth/wordWidth;
                int n= (num*3)/2;
                String dashes="";
                for (int i=0;i<n;i++){
                    dashes+="-";
                }
                sHistory += sCalculation+"\n="+sAnswer+"\n"+dashes+"\n";
                //sAnswer="";
                sCalculation="";
                sExpression="";
                current_number="";
                //updateCalculation();
                calculation.setText(sHistory);
                answer.setText(sAnswer);
                clr.setText("AC");
                scrollDown();
            }
            setClearBtnText();
        }
        catch (Exception e){

        }
    }

    public void onClickDelete(View view) {
        try {
            if (sAnswer.length() != 0) {
                if (sCalculation.length()>0) {
                    if (getcharfromLast(sCalculation, 1) != ' ') { //means there is number
                        sCalculation = sCalculation.substring(0, sCalculation.length() - 1);
                        sExpression=sCalculation;
                        if (current_number.length()>0)
                            current_number=current_number.substring(0,current_number.length()-1);
                        //mean there is an operator after removing last digit in above step
                        if (sExpression.length()>1 && getcharfromLast(sCalculation, 1) == ' '){
                            sExpression=sCalculation.substring(0, sCalculation.length() - 3);
                        }
                        //Mean There is dot after removing number
                        else if (sExpression.length()>1 && getcharfromLast(sCalculation, 1) == '.'){
                            sExpression=sCalculation.substring(0, sCalculation.length() - 1);
                        }
                        updateCalculation();
                        if (sCalculation.length()>0)
                            if (getcharfromLast(sCalculation, 1) == ' ')
                                current_operator=getcharfromLast(sCalculation,2)+"";
                        if (sCalculation.length()==0)
                        {
                            sAnswer="";
                            answer.setText("");
                            current_number="";
                        }
                        sExpression=sCalculation;
                    }
                    else{
                        sCalculation = sCalculation.substring(0, sCalculation.length() - 3);
                        current_number=getCurrenctNumber();
                        current_operator="";
                        sExpression=sCalculation;
                        updateCalculation();
                    }
                }
                else{
                    current_number="";
                    sAnswer="";
                    answer.setText("");
                    //updateCalculation();
                }
            }
            else {
                sAnswer="";
                answer.setText("");
            }
        }
        catch (Exception e){

        }


    }

    public String[] PercentOrModulo(){
        int length=sCalculation.length();
        int i=length-1;
        int j=-1;
        int opCount=0;
        int mulDivideOp=0;
        String[] str=new String[2];
        String[] str1=new String[1];
        String[] str2={};
        while(i>0)
        {
            char ch = sCalculation.charAt(i);
            boolean d=Character.isDigit(ch);
            boolean s=Character.isWhitespace(ch);
            if(s)
            {
                if(j==-1)
                    j=i;
            }
            boolean o=isOperator(ch);
            if(o)
            {

                if(ch=='x' || ch=='/'){
                    mulDivideOp++;
                    break;
                }
                if(ch=='+' || ch=='-')
                {
                    opCount++;
                    break;

                }
            }
            i--;
        }
        if (j!=-1){
            if(mulDivideOp==0 && opCount>0 && j!=-1){
                str[0] = (j-2)+"";
                str[1] = sCalculation.substring(j+1,length);
                return str;
            }
            else{
                str1[0]= (i+2)+"";
                return str1;
            }
        }
        else
            return str2;

    }

    private boolean isOperator(char ch) {
        boolean o=false;
        switch(ch)
        {
            case '+':
                o=true;
                break;
            case '-':
                o=true;
                break;
            case 'x':
                o=true;
                break;
            case '/':
                o=true;
                break;
            case '%':
                o=true;
                break;
        }
        return o;
    }

    private void scrollDown(){
        history.post(new Runnable() {
            @Override
            public void run() {
                history.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void setListeners(){
        zero= calculatorView.findViewById(R.id.btn_zero);
        one= calculatorView.findViewById(R.id.btn_one);
        two= calculatorView.findViewById(R.id.btn_two);
        three= calculatorView.findViewById(R.id.btn_three);
        four= calculatorView.findViewById(R.id.btn_four);
        five= calculatorView.findViewById(R.id.btn_five);
        six= calculatorView.findViewById(R.id.btn_six);
        seven= calculatorView.findViewById(R.id.btn_seven);
        eight= calculatorView.findViewById(R.id.btn_eight);
        nine= calculatorView.findViewById(R.id.btn_nine);
        clr= calculatorView.findViewById(R.id.btn_clear);
        per= calculatorView.findViewById(R.id.btn_modulo);
        div= calculatorView.findViewById(R.id.btn_division);
        mul= calculatorView.findViewById(R.id.btn_multiplication);
        minus= calculatorView.findViewById(R.id.btn_minus);
        plus= calculatorView.findViewById(R.id.btn_plus);
        equ= calculatorView.findViewById(R.id.btn_equal);
        dot= calculatorView.findViewById(R.id.btn_dot);
        del= calculatorView.findViewById(R.id.btn_delete);

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOperator(view);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOperator(view);
            }
        });
        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOperator(view);
            }
        });
        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOperator(view);
            }
        });
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickClear(view);
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDotClick(view);
            }
        });
        equ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEqual(view);
            }
        });
        per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onModuloClick(view);
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDelete(view);
            }
        });
    }

    private void setClearBtnText(){
        try {
            if (sCalculation.length()>0 && sAnswer.length()>0 && sHistory.length()>0){
                clr.setText("C");
            }
            else if (sHistory.length()>0 && sCalculation.length()==0 && sAnswer.length()==0){
                clr.setText("AC");
            }
            else {
                clr.setText("C");
            }
        }
        catch (Exception e){

        }
    }

    private String getCurrenctNumber(){
        int length=sCalculation.length();
        while(length>0){
            char c = sCalculation.charAt(length-1);
            if(c==' ')
                break;
            length--;
        }
        return sCalculation.substring(length);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //((MainActivity)getActivity()).canChangeOrientation(true);
    }
}