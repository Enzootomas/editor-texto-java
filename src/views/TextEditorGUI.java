package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;

import models.TextFile;

public class TextEditorGUI extends JFrame implements ActionListener {
    
    // Cria lista de arquivos
    private DefaultListModel<TextFile> fileListModel;
    private JList<TextFile> jFileList;

    // Componente para edição de texto
    private JTextArea txtArea;

    // Barra de status inferior
    private JLabel labelStatus;
    
    // Itens de Menu
    private JMenuItem itemNew;
    private JMenuItem itemRename;
    private JMenuItem itemClear;
    private JMenuItem itemDelete;

    // Botão
    private JButton btnSave;

    // Guarda a referência do objeto TextFile atualmente selecionado
    private TextFile arquivoAtual = null;

    // Construtor: Configura a interface gráfica
    public TextEditorGUI() {
        
        // Configurações básicas da janela
        super("Editor de texto");
        this.setSize(750, 750);
        this.setLayout(new BorderLayout());

        // Ativa o encerramento do programa pelo botão "X"
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 1. Painel Lateral (Lista de instâncias TextFile)
        fileListModel = new DefaultListModel<>();
        jFileList = new JList<>(fileListModel);
        jFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configura a lista para exibir o atributo name dos TextFile
        jFileList.setCellRenderer(new ListCellRenderer<TextFile>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends TextFile> list, TextFile arquivo, int index, boolean isSelected, boolean cellHasFocus) {
                
                JLabel label = new JLabel();
                label.setOpaque(false);
                if (arquivo != null) {
                    label.setText(arquivo.getName());
                }
                
                label.setFont(list.getFont());
                label.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
                
                // Gerenciamento explícito de cores de seleção e fundo
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                    label.setOpaque(true);
                } else {
                    label.setBackground(list.getBackground());
                    label.setForeground(list.getForeground());
                }
                
                return label;
            }
        });
        
        JPanel painelLateral = new JPanel(new BorderLayout());
        painelLateral.setPreferredSize(new Dimension(200, 750));
        painelLateral.setBorder(BorderFactory.createTitledBorder("Seus arquivos"));
        painelLateral.add(new JScrollPane(jFileList), BorderLayout.CENTER);

        // 2. Barra de Menus
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Arquivo");
        
        itemNew = new JMenuItem("Novo");
        itemRename = new JMenuItem("Renomear");
        itemClear = new JMenuItem("Limpar");
        itemDelete = new JMenuItem("Deletar");
        
        // Registrar esta própria classe como o Listener dos itens de menu
        itemNew.addActionListener(this);
        itemRename.addActionListener(this);
        itemClear.addActionListener(this);
        itemDelete.addActionListener(this);

        // Adiciona os JItemMenu ao JMenu
        menu.add(itemNew);
        menu.add(itemRename);
        menu.add(itemClear);
        menu.add(itemDelete);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        // 3. Área de Texto Central
        txtArea = new JTextArea();
        txtArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        //txtArea.setOpaque(true);
        JScrollPane painelTexto = new JScrollPane(txtArea);

        // Botão para salvar edições
        btnSave = new JButton("Salvar");
        btnSave.addActionListener(this); // Registrar o listener aqui também

        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.add(painelTexto, BorderLayout.CENTER);
        painelCentral.add(btnSave, BorderLayout.SOUTH);
        painelCentral.setBorder(new EmptyBorder(10,5, 0,15));

        // 4. Barra de Status Inferior
        JPanel barraStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barraStatus.setPreferredSize(new Dimension(750, 25));
        labelStatus = new JLabel(" Crie ou selecione um arquivo de texto.");
        barraStatus.add(labelStatus);

        // 5. Evento de Seleção da Lista (Mantido com Listener anônimo por conveniência do JList)
        jFileList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                arquivoAtual = jFileList.getSelectedValue();
                if (arquivoAtual != null) {
                    txtArea.setText(arquivoAtual.getContent());
                    labelStatus.setText(arquivoAtual.toString());
                }
            }
        });

        // Adiciona os painéis principais ao JFrame
        this.add(painelLateral, BorderLayout.WEST);
        this.add(painelCentral, BorderLayout.CENTER);
        this.add(barraStatus, BorderLayout.SOUTH);

        // Centraliza a janela na tela
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // Método obrigatório da interface ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        
        // Caso o usuário clique em "Novo"
        if (e.getSource() == itemNew) {
            try{
                TextFile novoArquivo = new TextFile((String)JOptionPane.showInputDialog(this, "Digite o nome do arquivo:"));
                fileListModel.addElement(novoArquivo);
                arquivoAtual = novoArquivo;
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro",0);
            }
        }
        
        // Caso o usuário clique em "Renomear"
        else if (e.getSource() == itemRename) {
            if (arquivoAtual != null) {
                String novoNome = JOptionPane.showInputDialog(this, "Digite o novo nome:", arquivoAtual.getName());
                try{
                    arquivoAtual.rename(novoNome); // Executa o método dos alunos
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(this, ex.getMessage(),"Erro",0);
                }
            }
        }
        
        // Caso o usuário clique em "Limpar"
        else if (e.getSource() == itemClear) {
            if (arquivoAtual != null) {
                arquivoAtual.clear();
                txtArea.setText(arquivoAtual.getContent());
            }
        }

        // Caso o usuário clique em "Deletar"
        else if (e.getSource() == itemDelete) {
            if (arquivoAtual != null){
                fileListModel.removeElement(arquivoAtual);
                arquivoAtual = fileListModel.getElementAt(0);
                txtArea.setText(arquivoAtual.getContent());
            }
        }

        // Caso o usuário clique no botão "Salvar"
        else if (e.getSource() == btnSave) {
            // Se o arquivo ainda não existir então cria
            if (arquivoAtual == null){
                try{
                    TextFile novoArquivo = new TextFile((String)JOptionPane.showInputDialog(this, "Digite o nome do arquivo:"),txtArea.getText());
                    fileListModel.addElement(novoArquivo);
                    arquivoAtual = novoArquivo;
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro",0);
                }
            }else{
                arquivoAtual.edit(txtArea.getText()); // Chama o método edit() passando o texto da tela 
            }         
        }

        labelStatus.setText(arquivoAtual.toString()); // Atualiza a barra de status
        jFileList.setSelectedValue(arquivoAtual, rootPaneCheckingEnabled);
        jFileList.repaint(); // Força a lista a atualizar
    }

}
