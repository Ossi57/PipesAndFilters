package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.stream.PushPipe;
import at.fhv.sysarch.lab3.pipeline.stream.Sink;
import at.fhv.sysarch.lab3.pipeline.stream.Source;
import at.fhv.sysarch.lab3.pipeline.stream.filters.*;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

import java.nio.channels.Pipe;

public class PushPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO: push from the source (model)
        Source src = new Source(pd.getModel().getFaces());
        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        ModelViewTransformationFilter modelViewTransformation = new ModelViewTransformationFilter(pd);
        PushPipe<Face> pipe7 = new PushPipe<Face>(modelViewTransformation);
        src.setSuccessor(pipe7);
        // TODO 2. perform backface culling in VIEW SPACE
        BackfaceCullingFilter backfaceCullingFilter = new BackfaceCullingFilter();
        PushPipe<Face> pipe6 = new PushPipe<Face>(backfaceCullingFilter);
        modelViewTransformation.setSuccessor(pipe6);
        // TODO 3. perform depth sorting in VIEW SPACE
        //not possible
        // TODO 4. add coloring (space unimportant)
        ColoringFilter coloringFilter = new ColoringFilter(pd);
        PushPipe<Face> pipe5 = new PushPipe<Face>(coloringFilter);
        backfaceCullingFilter.setSuccessor(pipe5);

        // lighting can be switched on/off
        PerspectiveProjectionFilter perspectiveProjectionFilter;
        LightingFilter lightingFilter;
        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
            lightingFilter = new LightingFilter(pd);
            PushPipe<Pair<Face, Color>> pipe4 = new PushPipe<Pair<Face, Color>>(lightingFilter);
            coloringFilter.setSuccessor(pipe4);
            // 5. TODO perform projection transformation on VIEW SPACE coordinates
            perspectiveProjectionFilter = new PerspectiveProjectionFilter(pd);
            PushPipe<Pair<Face, Color>> pipe3 = new PushPipe<Pair<Face, Color>>(perspectiveProjectionFilter);
            lightingFilter.setSuccessor(pipe3);

        } else {
            // 5. TODO perform projection transformation
            perspectiveProjectionFilter = new PerspectiveProjectionFilter(pd);
            PushPipe<Pair<Face, Color>> pipe2 = new PushPipe<Pair<Face, Color>>(perspectiveProjectionFilter);
            coloringFilter.setSuccessor(pipe2);
        }

        // TODO 6. perform perspective division to screen coordinates
        ScreenSpaceTransformationFilter screenSpaceTransformationFilter = new ScreenSpaceTransformationFilter(pd);
        PushPipe<Pair<Face, Color>> pipe2 = new PushPipe<Pair<Face, Color>>(screenSpaceTransformationFilter);
        perspectiveProjectionFilter.setSuccessor(pipe2);


        // TODO 7. feed into the sink (renderer)
        Sink sink = new Sink(pd);
        PushPipe<Pair<Face, Color>> pipe1 = new PushPipe<Pair<Face, Color>>(sink);
        screenSpaceTransformationFilter.setSuccessor(pipe1);

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
                // TODO create new model rotation matrix using pd.modelRotAxis
                if(fraction % 2*Math.PI >= 1)
                    radiant = 0;
                modelViewTransformation.setRotationMatrix(Matrices.rotate((radiant), pd.getModelRotAxis()));
                // TODO compute updated model-view tranformation

                // TODO update model-view filter

                // TODO trigger rendering of the pipeline
                src.write(model.getFaces());
            }
        };
    }
}