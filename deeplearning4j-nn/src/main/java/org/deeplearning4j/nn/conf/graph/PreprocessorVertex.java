/*-
 *
 *  * Copyright 2016 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package org.deeplearning4j.nn.conf.graph;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deeplearning4j.nn.conf.InputPreProcessor;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.inputs.InvalidInputTypeException;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;

/** PreprocessorVertex is a simple adaptor class that allows a {@link InputPreProcessor} to be used in a ComputationGraph
 * GraphVertex, without it being associated with a layer.
 * @author Alex Black
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class PreprocessorVertex extends GraphVertex {

    private InputPreProcessor preProcessor;

    /**
     * @param preProcessor The input preprocessor
     */
    public PreprocessorVertex(InputPreProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    @Override
    public GraphVertex clone() {
        return new PreprocessorVertex(preProcessor.clone());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PreprocessorVertex))
            return false;
        return ((PreprocessorVertex) o).preProcessor.equals(preProcessor);
    }

    @Override
    public int hashCode() {
        return preProcessor.hashCode();
    }

    @Override
    public int numParams(boolean backprop) {
        return 0;
    }

    @Override
    public org.deeplearning4j.nn.graph.vertex.GraphVertex instantiate(ComputationGraph graph, String name, int idx,
                    INDArray paramsView, boolean initializeParams) {
        return new org.deeplearning4j.nn.graph.vertex.impl.PreprocessorVertex(graph, name, idx, preProcessor);
    }

    @Override
    public InputType getOutputType(int layerIndex, InputType... vertexInputs) throws InvalidInputTypeException {
        if (vertexInputs.length != 1)
            throw new InvalidInputTypeException("Invalid input: Preprocessor vertex expects " + "exactly one input");

        return preProcessor.getOutputType(vertexInputs[0]);
    }
}
