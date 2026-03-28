package PedCompraIntegrator.ui;

import PedCompraIntegrator.config.EcoDbConnector;
import PedCompraIntegrator.dao.EmpresaDAO;
import PedCompraIntegrator.dao.FornecedorDAO;
import PedCompraIntegrator.dto.EcoDbConfig;
import PedCompraIntegrator.dto.Empresa;
import PedCompraIntegrator.dto.Fornecedor;
import PedCompraIntegrator.dto.ProdutoDTO;
import PedCompraIntegrator.service.ExcelReader;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class PedCompraMainFrame extends JFrame {

    private Connection con;

    public PedCompraMainFrame() {};

    public PedCompraMainFrame(Connection con) {
        this.con = con;
        initComponents();
        bindActions();
        buildLayout();
        bindActions();
        listarEmpresas();
    }

    // Campos da tela.
    private final JPopupMenu popUpFornecedor = new JPopupMenu();
    private final DefaultListModel<Fornecedor> modelFornecedores = new DefaultListModel<>();
    private final JList<Fornecedor> listFornecedores = new JList<>(modelFornecedores);

    private final DefaultComboBoxModel<Empresa> modelEmpresas = new DefaultComboBoxModel<>();

    private final JTextField tfFornecedor = new JTextField();
   // private final JComboBox cbEmpresaOrigem = new JComboBox();
    private final JComboBox<Empresa> cbEmpresaOrigem = new JComboBox<>();
    private final JTextField tfCaminhoPlanilha = new JTextField();
    private final JTextField tfEmailFornecedor = new JTextField();


    private final JButton btProcurarPlanilha = new  JButton("Procurar...");
    private final JButton btProcessar = new JButton("Processar");

    private final JLabel lbStatus = new JLabel("Preencha os campos para iniciar.");



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
        cbEmpresaOrigem.putClientProperty("JTextField.placeholdertext", "EX: 01");
        tfCaminhoPlanilha.putClientProperty("JTextField.placeholdertext", "Selecione a planilha...");
        tfEmailFornecedor.putClientProperty("JTextField.placeholdertext", "Ex: compras@fornecedor.com");

        // roundRect:
        // propriedade do FlatLaf para deixar o campo com cantos arredondados.
        tfFornecedor.putClientProperty("JComponent.roundRect", true);
        cbEmpresaOrigem.putClientProperty("JComponent.roundRect", true);
        tfCaminhoPlanilha.putClientProperty("JComponent.roundRect", true);
        tfEmailFornecedor.putClientProperty("JComponent.roundRect", true);

        listFornecedores.setFocusable(false);
        listFornecedores.setFixedCellHeight(28);
        listFornecedores.setVisibleRowCount(5);
        listFornecedores.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value.toString());
            label.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            label.setOpaque(true);

            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }

            return label;
        });
        //popUpFornecedor.putClientProperty("FlatLaf.style", "arc:16");
        popUpFornecedor.putClientProperty("FlatLaf.style", ""
                + "arc:16;"
                + "borderWidth:1;"
                + "focusWidth:0;"
        );
        popUpFornecedor.setBorder(BorderFactory.createEmptyBorder());


        JScrollPane scroll = new JScrollPane(listFornecedores);
        //scroll.putClientProperty("FlatLaf.style", "arc:16");
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.putClientProperty("JComponent.roundRect", true);
        scroll.putClientProperty("FlatLaf.style", ""
                + "arc:16;"
                + "focusWidth:0;"
        );
        popUpFornecedor.add(scroll);

        // O campo de caminho será preenchido pelo seletor de arquivo.
        // Então o usuário não precisa digitar nele manualmente.
        tfCaminhoPlanilha.setEditable(false);

        // Define um tamanho visual mais padrão para os campos.
        Dimension fieldSize = new Dimension(320, 38);
        tfFornecedor.setPreferredSize(fieldSize);
        cbEmpresaOrigem.setPreferredSize(fieldSize);
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
        gbc.insets = new Insets(6, 6, 6, 4); // Insets = margens externas de cada componente.
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
        formPanel.add(cbEmpresaOrigem, gbc);

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

        tfFornecedor.getDocument().addDocumentListener(new DocumentListener() {

            private void aoMudar(){
                String texto = tfFornecedor.getText();

                buscarFornecedor(texto);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                aoMudar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aoMudar();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aoMudar();

            }



        });

        listFornecedores.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    Fornecedor fornecedor = listFornecedores.getSelectedValue();

                    if (fornecedor != null) {
                        selecionarFornecedor(fornecedor);
                    }
                }
            }
        });

        listFornecedores.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 1) {

                    Fornecedor fornecedor = listFornecedores.getSelectedValue();

                    if (fornecedor != null) {
                        selecionarFornecedor(fornecedor);
                    }
                }
            }
        });
    }

    private void buscarFornecedor(String trecho){

        if (trecho.length() < 3){
            return;
        }

        FornecedorDAO fdao = new FornecedorDAO();

        try {
            List<Fornecedor> fornecedores = fdao.ListaFornecedores(con, trecho);

            modelFornecedores.clear();

            for (Fornecedor f : fornecedores){
                modelFornecedores.addElement(f);
            }
            if (modelFornecedores.isEmpty()){
                popUpFornecedor.setVisible(false);
            }else {
                popUpFornecedor.setPopupSize(tfFornecedor.getWidth(), 90);
                popUpFornecedor.show(tfFornecedor, 0, tfFornecedor.getHeight());;
            }
            tfFornecedor.requestFocusInWindow();

        }catch (SQLException e){
            e.printStackTrace();
            popUpFornecedor.setVisible(false);
        }

    }

    private void listarEmpresas(){

        EmpresaDAO edao = new EmpresaDAO();
        System.out.println("ENTROU NO METODO listarEmpresas");

        try {
            List<Empresa> empresas = edao.listaEmpresas(con);

            modelEmpresas.removeAllElements();

            for (Empresa e : empresas){
                modelEmpresas.addElement(e);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        cbEmpresaOrigem.setModel(modelEmpresas);

    }

    private void selecionarFornecedor (Fornecedor fornecedor){
        tfFornecedor.setText(fornecedor.nome());
        tfEmailFornecedor.setText(fornecedor.email());
        popUpFornecedor.setVisible(false);
    }




        private void escolherPlanilha(){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecione a planilha");
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Planilhas excel", "XLSX", "XLS");
            fileChooser.addChoosableFileFilter(filtro);
            fileChooser.setFileFilter(filtro);

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
        String empresaOrigem = cbEmpresaOrigem.toString().trim();
        String caminhoPlanilha = tfCaminhoPlanilha.getText().trim();
        String emailFornecedor = tfEmailFornecedor.getText().trim();

        Path caminho = Path.of(caminhoPlanilha);
        ExcelReader.produto(caminho);


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


























