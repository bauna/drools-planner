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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.WorkingMemory;
import org.drools.planner.core.localsearch.decider.acceptor.tabu.TabuPropertyEnabled;
import org.drools.planner.core.move.Move;
import org.drools.planner.examples.nurserostering.domain.ShiftAssignment;
import org.drools.planner.examples.nurserostering.domain.Employee;

public class EmployeeMultipleChangeMove implements Move, TabuPropertyEnabled {

    private Employee fromEmployee;
    private List<ShiftAssignment> shiftAssignmentList;
    private Employee toEmployee;

    public EmployeeMultipleChangeMove(Employee fromEmployee, List<ShiftAssignment> shiftAssignmentList, Employee toEmployee) {
        this.fromEmployee = fromEmployee;
        this.shiftAssignmentList = shiftAssignmentList;
        this.toEmployee = toEmployee;
    }

    public boolean isMoveDoable(WorkingMemory workingMemory) {
        return !ObjectUtils.equals(fromEmployee, toEmployee);
    }

    public Move createUndoMove(WorkingMemory workingMemory) {
        return new EmployeeMultipleChangeMove(toEmployee, shiftAssignmentList, fromEmployee);
    }

    public void doMove(WorkingMemory workingMemory) {
        for (ShiftAssignment shiftAssignment : shiftAssignmentList) {
            if (!shiftAssignment.getEmployee().equals(fromEmployee)) {
                throw new IllegalStateException("The shiftAssignment (" + shiftAssignment + ") should have the same employee ("
                        + fromEmployee + ") as the fromEmployee (" + fromEmployee + ").");
            }
            NurseRosteringMoveHelper.moveEmployee(workingMemory, shiftAssignment, toEmployee);
        }
    }

    public Collection<? extends Object> getTabuProperties() {
        return Collections.singletonList(shiftAssignmentList);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof EmployeeMultipleChangeMove) {
            EmployeeMultipleChangeMove other = (EmployeeMultipleChangeMove) o;
            return new EqualsBuilder()
                    .append(fromEmployee, other.fromEmployee)
                    .append(shiftAssignmentList, other.shiftAssignmentList)
                    .append(toEmployee, other.toEmployee)
                    .isEquals();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(fromEmployee)
                .append(shiftAssignmentList)
                .append(toEmployee)
                .toHashCode();
    }

    public String toString() {
        return shiftAssignmentList + " => " + toEmployee;
    }

}
