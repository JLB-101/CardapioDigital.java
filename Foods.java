import java.util.*;

public class EngelsFoods {

    static class Item {
        String nome;
        int preco;

        Item(String nome, int preco) {
            this.nome = nome;
            this.preco = preco;
        }
    }

    static class Pedido {
        String cliente;
        Map<Item, Integer> itens = new LinkedHashMap<>();

        Pedido(String cliente) {
            this.cliente = cliente;
        }

        int getTotal() {
            int total = 0;
            for (Map.Entry<Item, Integer> entry : itens.entrySet()) {
                total += entry.getKey().preco * entry.getValue();
            }
            return total;
        }

        void exibirResumo() {
            System.out.println("\n=== Resumo do Pedido de " + cliente + " ===");
            for (Map.Entry<Item, Integer> entry : itens.entrySet()) {
                Item item = entry.getKey();
                int qtd = entry.getValue();
                System.out.println(item.nome + " x" + qtd + " = " + (item.preco * qtd) + " MZN");
            }
            System.out.println("Total: " + getTotal() + " MZN");
        }
    }

    static Scanner scanner = new Scanner(System.in);
    static Map<String, List<Item>> cardapio = new LinkedHashMap<>();
    static Map<Integer, Pedido> pedidosRegistrados = new HashMap<>();

    public static void main(String[] args) {
        inicializarCardapio();

        System.out.println("=== Bem-vindo ao Restaurante Engels-foods ===");
        System.out.println("Dispomos de nossos serviços alimentícios em categorias:");

        Pedido pedidoAtual = novoPedido();

        boolean continuar = true;
        while (continuar) {
            exibirMenuCategorias();
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 0) {
                continuar = false;
            } else {
                String categoria = obterCategoriaPorNumero(opcao);
                if (categoria != null) {
                    navegarCategoria(categoria, pedidoAtual);
                } else {
                    System.out.println("Opção inválida.");
                }
            }
        }

        finalizarPedido(pedidoAtual);
        consultarPedidos();
    }

    static Pedido novoPedido() {
        System.out.print("Digite seu nome para começar o pedido: ");
        String nome = scanner.nextLine();
        return new Pedido(nome);
    }

    static void inicializarCardapio() {
        cardapio.put("Aperitivos", Arrays.asList(
            new Item("Batatas Fritas", 150),
            new Item("Camarão Empanado", 300),
            new Item("Bruschetta", 200),
            new Item("Bolinho de Queijo", 180)
        ));
        cardapio.put("Pratos Principais", Arrays.asList(
            new Item("Feijoada", 500),
            new Item("Frango Grelhado", 400),
            new Item("Lasanha", 350),
            new Item("Risoto", 450)
        ));
        cardapio.put("Bebidas", Arrays.asList(
            new Item("Água", 50),
            new Item("Refrigerante", 100),
            new Item("Sumo Natural", 120),
            new Item("Cerveja", 150)
        ));
        cardapio.put("Sobremesas", Arrays.asList(
            new Item("Pudim", 200),
            new Item("Gelado", 250),
            new Item("Mousse", 220),
            new Item("Frutas Tropicais", 180)
        ));
    }

    static void exibirMenuCategorias() {
        int i = 1;
        for (String categoria : cardapio.keySet()) {
            System.out.println(i + " - " + categoria);
            i++;
        }
        System.out.println("0 - Finalizar Pedido");
        System.out.print("Escolha uma categoria: ");
    }

    static String obterCategoriaPorNumero(int numero) {
        int i = 1;
        for (String categoria : cardapio.keySet()) {
            if (i == numero) return categoria;
            i++;
        }
        return null;
    }

    static void navegarCategoria(String categoria, Pedido pedidoAtual) {
        List<Item> itens = cardapio.get(categoria);
        System.out.println("\n--- " + categoria + " ---");
        for (int i = 0; i < itens.size(); i++) {
            System.out.println((i + 1) + " - " + itens.get(i).nome + " - " + itens.get(i).preco + " MZN");
        }
        System.out.println("0 - Voltar");

        while (true) {
            System.out.print("Escolha o número do item (ou 0 para voltar): ");
            int escolha = scanner.nextInt();
            if (escolha == 0) break;

            if (escolha >= 1 && escolha <= itens.size()) {
                Item item = itens.get(escolha - 1);
                System.out.print("Quantidade de \"" + item.nome + "\": ");
                int qtd = scanner.nextInt();
                pedidoAtual.itens.put(item, pedidoAtual.itens.getOrDefault(item, 0) + qtd);
            } else {
                System.out.println("Item inválido.");
            }
        }
    }

    static void finalizarPedido(Pedido pedidoAtual) {
        pedidoAtual.exibirResumo();

        System.out.print("Valor pago (MZN): ");
        int pago = scanner.nextInt();
        while (pago < pedidoAtual.getTotal()) {
            System.out.println("Valor insuficiente. Tente novamente:");
            pago = scanner.nextInt();
        }
        int troco = pago - pedidoAtual.getTotal();
        System.out.println("Troco: " + troco + " MZN");

        int codigo = new Random().nextInt(9000) + 1000;
        pedidosRegistrados.put(codigo, pedidoAtual);
        System.out.println("Código do seu pedido: " + codigo);
        System.out.println("Obrigado pela preferência, " + pedidoAtual.cliente + "! Volte sempre!");
    }

    static void consultarPedidos() {
        System.out.println("\nDeseja consultar seu pedido? (S/N)");
        String resposta = scanner.next().toUpperCase();
        if (resposta.equals("S")) {
            System.out.print("Digite seu código: ");
            int cod = scanner.nextInt();
            if (pedidosRegistrados.containsKey(cod)) {
                pedidosRegistrados.get(cod).exibirResumo();
            } else {
                System.out.println("Código não encontrado.");
            }
        }
    }
}