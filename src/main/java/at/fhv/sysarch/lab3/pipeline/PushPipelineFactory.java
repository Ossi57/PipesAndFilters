package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.stream.PushPipe;
import at.fhv.sysarch.lab3.pipeline.stream.Sink;
import at.fhv.sysarch.lab3.pipeline.stream.Source;
import at.fhv.sysarch.lab3.pipeline.stream.filters.BackfaceCullingFilter;
import at.fhv.sysarch.lab3.pipeline.stream.filters.ColoringFilter;
import at.fhv.sysarch.lab3.pipeline.stream.filters.ModelViewTransformationFilter;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

public class PushPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO: push from the source (model)
        Source src = new Source();
        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        ModelViewTransformationFilter modelViewTransformation = new ModelViewTransformationFilter(pd);
        // TODO 2. perform backface culling in VIEW SPACE
        BackfaceCullingFilter backfaceCullingFilter = new BackfaceCullingFilter();
        // TODO 3. perform depth sorting in VIEW SPACE
        //not possible
        // TODO 4. add coloring (space unimportant)
        ColoringFilter coloringFilter = new ColoringFilter(pd);
        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
            
            // 5. TODO perform projection transformation on VIEW SPACE coordinates
        } else {
            // 5. TODO perform projection transformation
        }

        // TODO 6. perform perspective division to screen coordinates

        // TODO 7. feed into the sink (renderer)
        Sink sink = new Sink(pd.getGraphicsContext());
        PushPipe<Pair<Face, Color>> pipe = new PushPipe<>(sink);
        coloringFilter.setSuccessor(pipe);
        PushPipe<Face> pipe1 = new PushPipe<Face>(coloringFilter);
        backfaceCullingFilter.setSuccessor(pipe1);
        PushPipe<Face> pipe2 = new PushPipe<Face>(backfaceCullingFilter);
        modelViewTransformation.setSuccessor(pipe2);
        PushPipe<Face> pipe3 = new PushPipe<Face>(modelViewTransformation);
        src.setSuccessor(pipe3);
        // returning an animation renderer which handles clearing of the
        // viewport and computation of the praction
        return new AnimationRenderer(pd) {
            // TODO rotation variable goes in here
            float radiant = 0;

            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer). 
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render 
             */
            @Override
            protected void render(float fraction, Model model) {
                // TODO compute rotation in radians
                radiant += fraction;
                if(fraction % 2*Math.PI >= 1)
                    radiant = 0;
                modelViewTransformation.setRotationMatrix(Matrices.rotate((radiant), pd.getModelRotAxis()));

                // TODO create new model rotation matrix using pd.modelRotAxis

                // TODO compute updated model-view tranformation

                // TODO update model-view filter

                // TODO trigger rendering of the pipeline
                src.write(model.getFaces());
            }
        };
    }
}