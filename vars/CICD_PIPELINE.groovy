import com.poc.util.checkoutSCM;

def call(Map pipelineParams)
{
    env.GIT_GROUP = pipelineParams.GIT_GROUP
    env.REPO = pipelineParams.REPO
    env.BRANCH = pipelineParams.BRANCH
    pipeline
    {
        node("master")
        {
            stage("code checkout")
            {
                echo 'Step to checkout the code from github'
                sh '''
                cd $WORKSPACE
                '''
                new checkoutSCM().call(pipelineParams)
            }
            stage("build code")
            {
            }
        }
    }
}
