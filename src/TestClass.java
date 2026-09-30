public class TestClass {

    public void publicNotAnnotated(String str) {
        System.out.println("[publicNotAnnotated] str = " + str);
    }

    @RepeatCount(5)
    public void publicAnnotated(int val) {
        System.out.println("[publicAnnotated] val = " + val);
    }

    @RepeatCount(3)
    protected void protectedAnnotated(String str, int val) {
        System.out.println("[protectedAnnotated] str = " + str + ", val = " + val);
    }

    protected void protectedNotAnnotated(double val) {
        System.out.println("[protectedNotAnnotated] val = " + val);
    }

    @RepeatCount(4)
    private void privateAnnotated(boolean bool) {
        System.out.println("[privateAnnotated] bool = " + bool);
    }

    private void privateNotAnnotated(String str) {
        System.out.println("[privateNotAnnotated] str = " + str);
    }
}