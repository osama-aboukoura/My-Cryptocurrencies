public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");

        Model model = new Model();

        View view = new View(model);
        view.setVisible(true);

    }

}
