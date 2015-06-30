/*
    Copyright (C) 2015   Martin Dames <martin@bastionbytes.de>
  
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
  
*/

package tingeltangel.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import tingeltangel.core.Repository;
import tingeltangel.core.constants.TxtFile;
import tingeltangel.tools.Progress;
import tingeltangel.tools.ProgressDialog;

/**
 *
 * @author mdames
 */
public class RepositoryManager extends javax.swing.JInternalFrame {

    private MyListModel model = new MyListModel();
    private MasterFrame masterFrame;
    
    /**
     * Creates new form RepositoryManager
     */
    public RepositoryManager(MasterFrame frame) {
        initComponents();
        this.masterFrame = frame;
        list.setModel(model);
        
        list.setCellRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                
                Map<String, String> txt = (Map<String, String>)value;
                
                JLabel label = new JLabel(txt.get("ID") + ": " + txt.get(TxtFile.KEY_NAME) + " (" + txt.get(TxtFile.KEY_AUTHOR) + ")");
                                
                if(Repository.exists(Integer.parseInt(txt.get("ID")))) {
                    label.setBackground(Color.green);
                    label.setOpaque(true);
                }
                return(label);
            }
        });
        update();
        
        final JList _list = list;
        
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    
                    new Progress(masterFrame, "Suche nach Aktualisierungen") {
                        @Override
                        public void action(ProgressDialog progressDialog) {
                            try {
                                int index = _list.locationToIndex(evt.getPoint());
                                Map<String, String> txt = (Map<String, String>)model.getElementAt(index);
                                int id = Integer.parseInt(txt.get("ID"));
                                if(Repository.exists(id)) {
                                    Repository.update(id, progressDialog);
                                } else {
                                    Repository.download(id, progressDialog);
                                }
                            } catch(IOException e) {
                                JOptionPane.showMessageDialog(RepositoryManager.this, "Download fehlgeschlagen: " + e.toString());
                                e.printStackTrace(System.out);
                            }
                            update();
                        }
                    };
                    
                    
                    
                    int index = list.locationToIndex(evt.getPoint());
                    Map<String, String> txt = (Map<String, String>)model.getElementAt(index);
                    int id = Integer.parseInt(txt.get("ID"));
                    try {
                        if(Repository.exists(id)) {
                            Repository.download(id, null);
                        } else {
                            Repository.update(id, null);
                        }
                    } catch(IOException ioe) {
                        JOptionPane.showMessageDialog(RepositoryManager.this, "Download fehlgeschlagen: " + ioe.getMessage());
                    }
                }
            }
        });
    }

    private void update() {
        model.update();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        update = new javax.swing.JMenuItem();
        search = new javax.swing.JMenuItem();

        list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(list);

        jMenu1.setText("Aktionen");

        update.setText("aktualisieren");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });
        jMenu1.add(update);

        search.setText("neue Bücher suchen");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });
        jMenu1.add(search);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        new Progress(masterFrame, "Suche nach Aktualisierungen") {
            @Override
            public void action(ProgressDialog progressDialog) {
                try {
                    Repository.update(progressDialog);
                } catch(IOException e) {
                    JOptionPane.showMessageDialog(RepositoryManager.this, "Suche nach Aktualisierungen fehlgeschlagen");
                    e.printStackTrace(System.out);
                } catch(IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(RepositoryManager.this, "Suche nach Aktualisierungen fehlgeschlagen: " + e.getMessage());
                }
                update();
            }
        };
        
    }//GEN-LAST:event_updateActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        new Progress(masterFrame, "Suche nach neuen Büchern") {
            @Override
            public void action(ProgressDialog progressDialog) {
                try {
                    Repository.search(progressDialog);
                } catch(IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(RepositoryManager.this, "Suche nach neuen Büchern fehlgeschlagen: " + e.getMessage());
                }
                update();
            }
        };
    }//GEN-LAST:event_searchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList list;
    private javax.swing.JMenuItem search;
    private javax.swing.JMenuItem update;
    // End of variables declaration//GEN-END:variables

    class MyListModel implements ListModel {
            
        private Set<ListDataListener> listeners = new HashSet<ListDataListener>();

        @Override
        public int getSize() {
            Integer[] ids = Repository.getIDs();
            return(ids.length);
        }

        @Override
        public Object getElementAt(int index) {
            int id = Repository.getIDs()[index];
            Map<String, String> txt = Repository.getBookTxt(id);

            String _id = Integer.toString(id);
            while(_id.length() < 5) {
                _id = "0" + _id;
            }

            txt.put("ID", _id);
            return(txt);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }

        void update() {
            Iterator<ListDataListener> i = listeners.iterator();
            while(i.hasNext()) {
                i.next().contentsChanged(null);
            }
        }

    };
}
