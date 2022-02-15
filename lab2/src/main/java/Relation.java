public enum Relation {
    EQUAL("="),
    LESS("<"),
    MORE(">"),
    Q("?");

    private final String str;

    Relation(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
