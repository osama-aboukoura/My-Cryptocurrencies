public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");

        Model model = new Model();
        Controller controller = new Controller();
        View view = new View(model, controller);

        view.setVisible(true);

    }

}
