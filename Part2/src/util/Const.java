package util;

public class Const {
    private String TYPE;
    private Object VALUE;

    public Const(String type, Object value) {
        this.TYPE = type;
        this.VALUE = value;
    }

    public String toString() {
        if (this.VALUE == null) return "<NIL>";
        return "<" + this.TYPE.toUpperCase() + ":" + this.VALUE + ">";
    }

    public boolean isString() {return this.VALUE instanceof String;}

    public boolean isNumber() {return this.VALUE instanceof Double;}
    
    public boolean isBool() {return this.VALUE instanceof Boolean;}
    
    public boolean isNil() {return this.VALUE == null;}

    public String getType() {return this.TYPE;}

    public Object getValue() {return this.VALUE;}

    public static void main(String[] args) {
        Const a = new Const("bool", true);
        Const b = new Const("bool", true);
        System.out.println(((Boolean) b.getValue()).equals((Boolean) a.getValue()));
        System.out.println(null != null);
        // Const a = new Const("STRING", teString);
    }
}