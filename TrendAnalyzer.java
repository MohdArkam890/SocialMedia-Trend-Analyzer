import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class TrendAnalyzer extends JFrame {

    private JTextField topicField;
    private JTable trendTable;
    private DefaultTableModel tableModel;
    
    // Simulated social media posts
    private List<String> posts = Arrays.asList(
        "Java is trending today #java #programming",
        "Learning DSA using Java #java #DSA",
        "AI and Machine Learning are hot topics #AI #ML",
        "Java Swing GUI projects are fun #java #swing",
        "Top programming languages #java #python #cpp",
        "Python is popular for AI #python #AI",
        "DSA is important for coding interviews #DSA #coding",
        "Java and Spring Boot projects #java #spring"
    );

    public TrendAnalyzer() {
        setTitle("Social Media Trend Analyzer - DSA Mini Project");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel - Input
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter Topic/Hashtag:"));
        topicField = new JTextField(20);
        inputPanel.add(topicField);
        JButton analyzeButton = new JButton("Analyze Trends");
        inputPanel.add(analyzeButton);
        add(inputPanel, BorderLayout.NORTH);

        // Table to show trends
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Keyword/Hashtag");
        tableModel.addColumn("Frequency");
        trendTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(trendTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Trending Keywords"));
        add(scrollPane, BorderLayout.CENTER);

        // Button action
        analyzeButton.addActionListener(e -> analyzeTrends());

        setVisible(true);
    }

    private void analyzeTrends() {
        String topic = topicField.getText().trim().toLowerCase();
        if(topic.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a topic!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Count keyword frequency using HashMap
        HashMap<String, Integer> freqMap = new HashMap<>();
        for(String post : posts) {
            String[] words = post.toLowerCase().split("\\s+");
            for(String word : words) {
                if(word.contains(topic)) {
                    freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
                }
            }
        }

        if(freqMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No trends found for this topic.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Sort by frequency using PriorityQueue (Max Heap)
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
                (a, b) -> b.getValue() - a.getValue()
        );
        pq.addAll(freqMap.entrySet());

        // Clear previous table data
        tableModel.setRowCount(0);

        // Show top trends
        int count = 0;
        while(!pq.isEmpty() && count < 10) {
            Map.Entry<String, Integer> entry = pq.poll();
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
            count++;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrendAnalyzer());
    }
}
