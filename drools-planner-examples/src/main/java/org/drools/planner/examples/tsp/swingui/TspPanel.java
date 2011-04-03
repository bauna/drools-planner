/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.planner.examples.tsp.swingui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.drools.planner.examples.common.swingui.SolutionPanel;
import org.drools.planner.examples.tsp.domain.CityAssignment;
import org.drools.planner.examples.tsp.domain.TravelingSalesmanTour;

/**
 * TODO this code is highly unoptimized
 */
public class TspPanel extends SolutionPanel {

    private static final Color HEADER_COLOR = Color.YELLOW;

    private GridLayout gridLayout;

    public TspPanel() {
        gridLayout = new GridLayout(0, 1);
        setLayout(gridLayout);
    }

    private TravelingSalesmanTour getTravelingSalesmanTour() {
        return (TravelingSalesmanTour) solutionBusiness.getSolution();
    }

    public void resetPanel() {
        removeAll();
        TravelingSalesmanTour travelingSalesmanTour = getTravelingSalesmanTour();
        JLabel headerLabel = new JLabel("Tour of " + travelingSalesmanTour.getName());
        headerLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        headerLabel.setBackground(HEADER_COLOR);
        headerLabel.setOpaque(true);
        add(headerLabel);
        if (travelingSalesmanTour.isInitialized()) {
            for (CityAssignment cityAssignment : travelingSalesmanTour.getCityAssignmentList()) {
                JPanel cityAssignmentPanel = new JPanel(new GridLayout(1, 2));
                JButton button = new JButton(new CityAssignmentAction(cityAssignment));
                cityAssignmentPanel.add(button);
                JLabel distanceLabel = new JLabel("Distance to next: " + cityAssignment.getCity().getDistance(
                        cityAssignment.getNextCityAssignment().getCity()));
                cityAssignmentPanel.add(distanceLabel);
                add(cityAssignmentPanel);
            }
        }
    }

    private class CityAssignmentAction extends AbstractAction {

        private CityAssignment cityAssignment;

        public CityAssignmentAction(CityAssignment cityAssignment) {
            super(cityAssignment.getCity().toString());
            this.cityAssignment = cityAssignment;
        }

        public void actionPerformed(ActionEvent e) {
            JPanel listFieldsPanel = new JPanel(new GridLayout(2, 1));
            List<CityAssignment> nextCityAssignmentList = getTravelingSalesmanTour().getCityAssignmentList();
            JComboBox nextCityAssignmentListField = new JComboBox(nextCityAssignmentList.toArray());
            nextCityAssignmentListField.setSelectedItem(cityAssignment.getNextCityAssignment());
            listFieldsPanel.add(nextCityAssignmentListField);
            int result = JOptionPane.showConfirmDialog(TspPanel.this.getRootPane(), listFieldsPanel,
                    "Select next city", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                CityAssignment toNextCityAssignement = (CityAssignment) nextCityAssignmentListField.getSelectedItem();
                solutionBusiness.doMove(new NextCityAssignmentChangeMove(cityAssignment, toNextCityAssignement));
                workflowFrame.updateScreen();
            }
        }

    }

}