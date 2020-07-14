package in.technostack.projects.converterforall.DataModels;

public class ValueObject {
    Double Base;
    Double value;

    public ValueObject(Double b, Double v){
        this.Base=b;
        this.value=v;
    }

    public Double getBase() {
        return Base;
    }

    public Double getValue() {
        return value;
    }
}
