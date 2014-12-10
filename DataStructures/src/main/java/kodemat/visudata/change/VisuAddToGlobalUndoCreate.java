/* 
 * Copyright 2014 Orthodoxos Kipouridis and Moritz Roidl.
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
package kodemat.visudata.change;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import kodemat.visudata.VisuChange;
import kodemat.visudata.VisuVector3f;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuAddToGlobalUndoCreate extends AbstractVisuAttributeChange<VisuVector3f>{
    
    public VisuDeleteComponent vdc;
    public boolean isUndo;

    public VisuAddToGlobalUndoCreate() {
    }

    public VisuAddToGlobalUndoCreate(long id) {
        super(id);
    }

    @Override
    public VisuVector3f getValue() {
        return null;
    }

    @Override
    public void setValue(VisuVector3f t) {
    }

    @Override
    public void writeData(ObjectDataOutput d) throws IOException {
        super.writeData(d);
        vdc.writeData(d);
    }

    @Override
    public void readData(ObjectDataInput di) throws IOException {
        super.readData(di);
        vdc = new VisuDeleteComponent();
        vdc.readData(di);
    }

    @Override
    public short getType() {
        return VisuChange.ADD_TO_GLOBAL_UNDO_CREATE;
    }

    @Override
    public long getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setId(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}