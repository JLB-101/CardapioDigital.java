
import java.util.*;

class Item {
    String nome;
    int preco;

    Item(String nome, int preco) {
        this.nome = nome;
        this.preco = preco;
    }
}

class Pedido {
    String nomeCliente;
    Map<Item, Integer> itens = new LinkedHashMap<>();

    Pedido(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    void adicionarItem(Item item, int quantidade) {
        itens.put(item, itens.getOrDefault(item, 0) + quantidade);
    }

    int calcularTotal() {
        int total = 0;
        for (Map.Entry<Item, Integer> entry : itens.entrySet()) {
            total += entry.getKey().preco * entry.getValue();
        }
        return total;
    }

    void exibirResumo() {
        System.out.println("Resumo do pedido de " + nomeCliente + ":");
        for (Map.Entry<Item, Integer> entry : itens.entrySet()) {
            System.out.println(entry.getKey().nome + " x " + entry.getValue() + " = " + (entry.getKey().preco * entry.getValue()) + " MZN");
        }
        System.out.println("Total: " + calcularTotal() + " MZN");
    }
}

public class CardapioDigital {
    static Map<String, List<Item>> menu = new LinkedHashMap<>();
    static Map<Integer, Pedido> pedidos = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    public static void main(String[] args) {
        inicializarMenu();  // Carrega o cardápio com categorias e itens
        Pedido pedido = new Pedido("");  // Criamos um pedido com nome vazio temporariamente

        while (true) {
            System.out.println("=== Bem-vindo ao Restaurante Engels-foods ===");
            System.out.println("Escolha uma categoria (ou 0 para Finalizar Pedido):");
            int index = 1;
            List<String> categorias = new ArrayList<>(menu.keySet());
            for (String categoria : categorias) {
                System.out.println(index + " - " + categoria);
                index++;
            }
            System.out.print("Opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha

            if (opcao == 0) {
                System.out.print("Digite seu nome: ");
                pedido.nomeCliente = scanner.nextLine();

                pedido.exibirResumo();
                System.out.print("Valor pago: ");
                int pago = scanner.nextInt();
                int troco = pago - pedido.calcularTotal();
                System.out.println("Troco: " + troco + " MZN");

                int codigo = random.nextInt(9000) + 1000;
                pedidos.put(codigo, pedido);
                System.out.println("Seu código de pedido é: " + codigo);
                System.out.println("Obrigado pela preferência! Volte sempre!");
                break;
            }

            if (opcao > 0 && opcao <= categorias.size()) {
                String categoriaSelecionada = categorias.get(opcao - 1);
                List<Item> itens = menu.get(categoriaSelecionada);
                System.out.println("Itens de " + categoriaSelecionada + ":");
                for (int i = 0; i < itens.size(); i++) {
                    System.out.println((i + 1) + " - " + itens.get(i).nome + " - " + itens.get(i).preco + " MZN");
                }
                System.out.print("Digite o número do item (ou 0 para voltar): ");
                int escolhaItem = scanner.nextInt();
                if (escolhaItem == 0) continue;

                System.out.print("Quantidade: ");
                int quantidade = scanner.nextInt();

                Item itemEscolhido = itens.get(escolhaItem - 1);
                pedido.adicionarItem(itemEscolhido, quantidade);
            } else {
                System.out.println("Opção inválida!");
            }
        }
    }

    static void inicializarMenu() {
        menu.put("Aperitivos", Arrays.asList(
                new Item("Batata Frita", 150),
                new Item("Pão de Alho", 100),
                new Item("Mini Pastéis", 120),
                new Item("Camarão Empanado", 200)
        ));
        menu.put("Pratos Principais", Arrays.asList(
                new Item("Feijoada", 200),
                new Item("Frango Grelhado", 400),
                new Item("Lasanha", 350),
                new Item("Matapa", 170)
        ));
        menu.put("Bebidas", Arrays.asList(
                new Item("Água", 50),
                new Item("Refrigerante", 100),
                new Item("Sumo Natural", 150),
                new Item("Cerveja txilar", 55)
        ));
        menu.put("Sobremesas", Arrays.asList(
                new Item("Pudim", 150),
                new Item("Mousse de Chocolate", 180),
                new Item("Sorvete", 130),
                new Item("Torta de Maçã", 200)
        ));
    }
}
