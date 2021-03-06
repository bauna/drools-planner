/*
 * Copyright 2010 JBoss Inc
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

package org.drools.planner.examples.nurserostering.solver.move;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.WorkingMemory;
import org.drools.planner.core.localsearch.decider.acceptor.tabu.TabuPropertyEnabled;
import org.drools.planner.core.move.Move;
import org.drools.planner.examples.nurserostering.domain.ShiftAssignment;
import org.drools.planner.examples.nurserostering.domain.Employee;

public class ShiftAssignmentSwitchMove implements Move, TabuPropertyEnabled {

    private ShiftAssignment leftShiftAssignment;
    private ShiftAssignment rightShiftAssignment;

    public ShiftAssignmentSwitchMove(ShiftAssignment leftShiftAssignment, ShiftAssignment rightShiftAssignment) {
        this.leftShiftAssignment = leftShiftAssignment;
        this.rightShiftAssignment = rightShiftAssignment;
    }

    public boolean isMoveDoable(WorkingMemory workingMemory) {
        return !ObjectUtils.equals(leftShiftAssignment.getEmployee(), rightShiftAssignment.getEmployee());
    }

    public Move createUndoMove(WorkingMemory workingMemory) {
        return new ShiftAssignmentSwitchMove(rightShiftAssignment, leftShiftAssignment);
    }

    public void doMove(WorkingMemory workingMemory) {
        Employee oldLeftEmployee = leftShiftAssignment.getEmployee();
        Employee oldRightEmployee = rightShiftAssignment.getEmployee();
        NurseRosteringMoveHelper.moveEmployee(workingMemory, leftShiftAssignment, oldRightEmployee);
        NurseRosteringMoveHelper.moveEmployee(workingMemory, rightShiftAssignment, oldLeftEmployee);
    }

    public Collection<? extends Object> getTabuProperties() {
        return Arrays.<ShiftAssignment>asList(leftShiftAssignment, rightShiftAssignment);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof ShiftAssignmentSwitchMove) {
            ShiftAssignmentSwitchMove other = (ShiftAssignmentSwitchMove) o;
            return new EqualsBuilder()
                    .append(leftShiftAssignment, other.leftShiftAssignment)
                    .append(rightShiftAssignment, other.rightShiftAssignment)
                    .isEquals();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(leftShiftAssignment)
                .append(rightShiftAssignment)
                .toHashCode();
    }

    public String toString() {
        return leftShiftAssignment + " <=> " + rightShiftAssignment;
    }

}
