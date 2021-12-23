package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.stream.PullPipe;
import at.fhv.sysarch.lab3.pipeline.stream.PushPipe;
import at.fhv.sysarch.lab3.pipeline.stream.Sink;
import at.fhv.sysarch.lab3.pipeline.stream.Source;
import at.fhv.sysarch.lab3.pipeline.stream.filters.*;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

import java.nio.channels.Pipe;

public class PullPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO: push from the source (model)
        Source src = new Source(pd.getModel().getFaces());
        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        ModelViewTransformationFilter modelViewTransformation = new ModelViewTransformationFilter(pd);
        PullPipe<Face> pipe7 = new PullPipe<>(src);
        modelViewTransformation.setPredecessor(pipe7);
        // TODO 2. perform backface culling in VIEW SPACE
        BackfaceCullingFilter backfaceCullingFilter = new BackfaceCullingFilter();
        PullPipe<Face> pipe6 = new PullPipe<>(modelViewTransformation);
        backfaceCullingFilter.setPredecessor(pipe6);
        // TODO 3. perform depth sorting in VIEW SPACE
        DepthSortingFilter depthSort = new DepthSortingFilter();
        PullPipe<Face> pipe65 = new PullPipe<Face>(backfaceCullingFilter);
        depthSort.setPredecessor(pipe65);
        // TODO 4. add coloring (space unimportant)
        ColoringFilter coloringFilter = new ColoringFilter(pd);
        PullPipe<Face> pipe5 = new PullPipe<>(depthSort);
        coloringFilter.setPredecessor(pipe5);

        // lighting can be switched on/off
        PerspectiveProjectionFilter perspectiveProjectionFilter;
        LightingFilter lightingFilter;
        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
            lightingFilter = new LightingFilter(pd);
            PullPipe<Pair<Face, Color>> pipe4 = new PullPipe<>(coloringFilter);
            lightingFilter.setPredecessor(pipe4);
            // 5. TODO perform projection transformation on VIEW SPACE coordinates
            perspectiveProjectionFilter = new PerspectiveProjectionFilter(pd);
            PullPipe<Pair<Face, Color>> pipe3 = new PullPipe<>(lightingFilter);
            perspectiveProjectionFilter.setPredecessor(pipe3);

        } else {
            // 5. TODO perform projection transformation
            perspectiveProjectionFilter = new PerspectiveProjectionFilter(pd);
            PullPipe<Pair<Face, Color>> pipe2 = new PullPipe<>(coloringFilter);
            perspectiveProjectionFilter.setPredecessor(pipe2);
        }

        // TODO 6. perform perspective division to screen coordinates
        ScreenSpaceTransformationFilter screenSpaceTransformationFilter = new ScreenSpaceTransformationFilter(pd);
        PullPipe<Pair<Face, Color>> pipe2 = new PullPipe<>(perspectiveProjectionFilter);
        screenSpaceTransformationFilter.setPredecessor(pipe2);


        // TODO 7. feed into the sink (renderer)
        Sink sink = new Sink(pd);
        PullPipe<Pair<Face, Color>> pipe1 = new PullPipe<>(screenSpaceTransformationFilter);
        sink.setPredecessor(pipe1);

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
                // TODO compute updated model-view tranformation
                // TODO update model-view filter
                if(fraction % 2*Math.PI >= 1)
                    radiant = 0;
                modelViewTransformation.setRotationMatrix(Matrices.rotate((radiant), pd.getModelRotAxis()));
                // TODO trigger rendering of the pipeline
                sink.read();
            }
        };
    }
}