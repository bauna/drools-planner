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

package org.drools.planner.examples.machinereassignment.domain.solver;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.planner.examples.machinereassignment.domain.MrMachine;
import org.drools.planner.examples.machinereassignment.domain.MrMachineCapacity;
import org.drools.planner.examples.machinereassignment.domain.MrResource;
import org.drools.planner.examples.machinereassignment.domain.MrService;

public class MrServiceMovedProcessesCount implements Serializable, Comparable<MrServiceMovedProcessesCount> {

    private MrService service;
    private int movedProcessesCount;

    public MrServiceMovedProcessesCount(MrService service, int movedProcessesCount) {
        this.service = service;
        this.movedProcessesCount = movedProcessesCount;
    }

    public MrService getService() {
        return service;
    }

    public int getMovedProcessesCount() {
        return movedProcessesCount;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof MrServiceMovedProcessesCount) {
            MrServiceMovedProcessesCount other = (MrServiceMovedProcessesCount) o;
            return new EqualsBuilder()
                    .append(service, other.service)
                    .append(movedProcessesCount, other.movedProcessesCount)
                    .isEquals();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(service)
                .append(movedProcessesCount)
                .toHashCode();
    }

    // Used by the GUI to sort the ConstraintOccurrence list by causes
    public int compareTo(MrServiceMovedProcessesCount other) {
        return new CompareToBuilder()
                .append(service, other.service)
                .append(movedProcessesCount, other.movedProcessesCount)
                .toComparison();
    }

    public Long getServiceId() {
        return service.getId();
    }

    @Override
    public String toString() {
        return service + "=" + movedProcessesCount;
    }

}
