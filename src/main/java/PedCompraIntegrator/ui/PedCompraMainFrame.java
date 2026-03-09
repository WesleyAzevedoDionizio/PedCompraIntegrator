package PedCompraIntegrator.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;


public class PedCompraMainFrame extends JFrame {

    // Campos da tela.
    private final JTextField tfFornecedor = new JTextField();
    private final JTextField tfEmpresaOrigem = new JTextField();
    private final JTextField tfCaminhoPlanilha = new JTextField();
    private final JTextField tfEmailFornecedor = new JTextField();

    private final JButton btProcurarPlanilha = new  JButton("Procurar...");
    private final JButton btProcessar = new JButton("Processar");

    private final JLabel lbStatus = new JLabel("Preencha os campos para iniciar.");

    public PedCompraMainFrame() {
        initFrame();
        initComponents();
        buildLayout();
        bindActions();
    }

    private JLabel createLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.putClientProperty("FlatLaf.style", "font: 105% bold");
        return label;
    }

    private void initFrame (){
        setTitle("PedCompraIntegrator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(720,420);

        // Centraliza a janela na tela.
        setLocationRelativeTo(null);

        // Permite redimensionamento.
        // Pode deixar false depois, se quiser impedir redimensionar.
        setResizable(true);
    }

    /**
     * Configurações dos componentes:
     * placeholders, tamanhos, estilos visuais e estados iniciais.
     */
    private void initComponents(){

        // Placeholders:
        // texto de dica que aparece no campo enquanto ele está vazio.
        tfFornecedor.putClientProperty("JTextField.placeholdertext", "Ex: Fornecedor:XPTO");
        tfEmpresaOrigem.putClientProperty("JTextField.placeholdertext", "EX: 01");
        tfCaminhoPlanilha.putClientProperty("JTextField.placeholdertext", "Selecione a planilha...");
        tfEmailFornecedor.putClientProperty("JTextField.placeholdertext", "Ex: compras@fornecedor.com");

        // roundRect:
        // propriedade do FlatLaf para deixar o campo com cantos arredondados.
        tfFornecedor.putClientProperty("JComponent.roundRect", true);
        tfEmpresaOrigem.putClientProperty("JComponent.roundRect", true);
        tfCaminhoPlanilha.putClientProperty("JComponent.roundRect", true);
        tfEmailFornecedor.putClientProperty("JComponent.roundRect", true);

        // O campo de caminho será preenchido pelo seletor de arquivo.
        // Então o usuário não precisa digitar nele manualmente.
        tfCaminhoPlanilha.setEditable(false);

        // Define um tamanho visual mais padrão para os campos.
        Dimension fieldSize = new Dimension(320, 38);
        tfFornecedor.setPreferredSize(fieldSize);
        tfEmpresaOrigem.setPreferredSize(fieldSize);
        tfCaminhoPlanilha.setPreferredSize(fieldSize);
        tfEmailFornecedor.setPreferredSize(fieldSize);

        // Estilo dos botões com FlatLaf.
        btProcurarPlanilha.putClientProperty("JButton.buttonType", "roundRect");
        btProcessar.putClientProperty("JButton.buttonType", "roundRect");

        // Dá um destaque maior para o botão principal.
        btProcessar.setPreferredSize(new Dimension(180, 40));

        // Estilo do texto de status.
        lbStatus.putClientProperty("FlatLaf.style", "font: 105%");

    }

    /**
     * Monta visualmente a tela.
     * Aqui criamos os painéis e organizamos os componentes na janela.
     */

    private void buildLayout(){
        // Painel raiz da janela.
        // BorderLayout divide a tela em regiões:
        // NORTH (topo), CENTER (centro), SOUTH (rodapé), WEST, EAST.

        JPanel root = new JPanel(new BorderLayout(0, 15));
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(root);

        // ===== TOPO =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        JLabel lbTitulo = new JLabel("Importador de pedido de compra");
        lbTitulo.putClientProperty("FlatLaf.style", "font: 170% bold");

        JLabel lbSubTitulo = new JLabel("Selecione a planilha, informe os dados principais e inicie o processo.");
        lbSubTitulo.putClientProperty("FlatLaf.style", "foreground: #6C757D");

        topPanel.add(lbTitulo);
        topPanel.add(Box.createVerticalStrut(6));//define espaço vertical
        topPanel.add(lbSubTitulo);

        //========centro========
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));


        // GridBagConstraints é o objeto que controla a posição dos componentes
        // dentro do GridBagLayout.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Insets = margens externas de cada componente.
        gbc.fill = GridBagConstraints.HORIZONTAL;// fill = faz o componente crescer horizontalmente.
        gbc.weightx = 1.0;// weightx = diz quanto o componente pode expandir no eixo horizontal.

        // Linha 0 - Fornecedor
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Fornecedor:"), gbc);

        gbc.gridx = 1;
        formPanel.add(tfFornecedor, gbc);

        // Linha 1 - Empresa origem
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createLabel("Empresa origem:"), gbc);

        gbc.gridx = 1;
        formPanel.add(tfEmpresaOrigem, gbc);

        // Linha 2 - Caminho da planilha
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createLabel("Planilha:"), gbc);


        JPanel planilhaPanel = new JPanel(new BorderLayout(8, 0));
        planilhaPanel.setOpaque(false);
        planilhaPanel.add(tfCaminhoPlanilha, BorderLayout.CENTER);
        planilhaPanel.add(btProcurarPlanilha, BorderLayout.EAST);

        gbc.gridx = 1;
        formPanel.add(planilhaPanel, gbc);

        // Linha 3 - Email fornecedor
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createLabel("E-mail do fornecedor:"), gbc);

        gbc.gridx = 1;
        formPanel.add(tfEmailFornecedor, gbc);

        // ===== RODAPÉ =====
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);

        lbStatus.setBorder(new EmptyBorder(8, 4, 8, 4));
        bottomPanel.add(lbStatus, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(btProcessar);

        bottomPanel.add(actionPanel, BorderLayout.EAST);

        // Adiciona tudo ao painel principal
        root.add(topPanel, BorderLayout.NORTH);
        root.add(formPanel, BorderLayout.CENTER);
        root.add(bottomPanel, BorderLayout.SOUTH);

    }

    private void bindActions() {

        btProcurarPlanilha.addActionListener(e -> escolherPlanilha());
        btProcessar.addActionListener(e -> processarFormulario());
    }


        private void escolherPlanilha(){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecione a planilha");

            int resultado  =  fileChooser.showOpenDialog(this);

            if (resultado == JFileChooser.APPROVE_OPTION){
                File arquivoSelecionado = fileChooser.getSelectedFile();
                tfCaminhoPlanilha.setText(arquivoSelecionado.getAbsolutePath());
                lbStatus.setText("Planilha selecionada corretamente");
        } else {
                lbStatus.setText("Seleção de planilha cancelada");
            }
    }

    /**
     * Primeira versão do processamento.
     * Por enquanto, apenas valida e mostra os dados.
     * Depois essa lógica sairá da UI e irá para uma classe de service.
     */
    private void processarFormulario() {

        String fornecedor = tfFornecedor.getText().trim();
        String empresaOrigem = tfEmpresaOrigem.getText().trim();
        String caminhoPlanilha = tfCaminhoPlanilha.getText().trim();
        String emailFornecedor = tfEmailFornecedor.getText().trim();

        //Validação simples
        if (fornecedor.isEmpty()){
            mostrarErro("Informe o fornecedor");
            return;
        }
        if (empresaOrigem.isEmpty()){
            mostrarErro("Informe a empresa origem");
            return;
        }
        if (caminhoPlanilha.isEmpty()){
            mostrarErro("Selecione a planilha");
            return;
        }
        if (emailFornecedor.isEmpty()){
            mostrarErro("Informe o email do fornecedor");
            return;
        }

        // Apenas para teste inicial.
        // Depois isso será substituído pela chamada do service.
        String mensagem = """
                Dados informados:
                Fornecedor: %s
                Empresa origem: %s
                Planilha: %s
                E-mail: %s
                """.formatted(fornecedor, empresaOrigem, caminhoPlanilha, emailFornecedor);

        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Resumo inicial",
                JOptionPane.INFORMATION_MESSAGE
        );

        lbStatus.setText("Dados validados. Próximo passo: integrar com services.");

    }
    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Validação",
                JOptionPane.WARNING_MESSAGE
        );
        lbStatus.setText(mensagem);
    }

}


























