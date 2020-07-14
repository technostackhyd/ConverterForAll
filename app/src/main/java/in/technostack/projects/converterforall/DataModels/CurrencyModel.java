package in.technostack.projects.converterforall.DataModels;

import java.io.Serializable;


public class CurrencyModel implements Serializable {
    public int currFlag;
    public String currName;
    public boolean isSelected;
    public CurrencyModel(int flag,String name){
        this.currFlag=flag;
        this.currName=name;
    }

    public CurrencyModel(int flag, String name, boolean isSelected){
        this.currFlag=flag;
        this.currName=name;
        this.isSelected=isSelected;
    }
    public int getCurrFlag() {
        return currFlag;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }


    public String getCurrName() {
        return currName;
    }

}
