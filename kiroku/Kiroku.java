package kiroku;

/**
 * This is the main entry point of the program.
 * <p>
 * It has the main method which creates and starts
 * an instance of {@link KirokuController}.
 * </p>
 * 
 * @see KirokuController#start()
 */
public class Kiroku {
    public static void main(String[] args) {
        KirokuController controller = new KirokuController();
        controller.start();
    }
}
