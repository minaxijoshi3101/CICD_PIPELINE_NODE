import com.poc.util.checkoutSCM;

def call(Map pipelineParams)
{
    env.GIT_GROUP = pipelineParams.GIT_GROUP
    env.REPO = pipelineParams.REPO
    pipeline
    {
        node("master")
        {
            stage("code checkout")
            {
                new checkoutSCM().call(pipelineParams);
            }
        }
    }
}