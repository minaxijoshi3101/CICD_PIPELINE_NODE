import com.poc.util.checkoutSCM;

def call(Map pipelineParams)
{
    env.GIT_GROUP = pipelineParams.GIT_GROUP
    env.REPO = pipelineParams.REPO
    env.BRANCH = pipelineParams.BRANCH
    env.REGISTRY = pipelineParams.REGISTRY
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
                sh '''
                cd $REPO
                npm install
                docker build -t node_app_image .
                docker images
                '''
            }
            stage("push image to docker ECR")
            {
                echo "stage to push image"
                sh '''
                #docker tag dockerrepo:latest 431078536743.dkr.ecr.ap-south-1.amazonaws.com/dockerrepo:latest
                docker image tag node_app_image ${REGISTRY}:node_app_imagev1.0
                LOGIN=$(aws ecr get-login --no-include-email --region ap-south-1)
                $LOGIN
                docker push ${REGISTRY}:node_app_imagev1.0
                '''
            }
        }
        stage("deploy")
        {
            echo "pull the image from ECR"
            sh '''
            LOGIN=$(aws ecr get-login --no-include-email --region ap-south-1)
            $LOGIN
            docker image pull ${REGISTRY}:node_app_imagev1.0
            docker rm -f node-app-container | error=true
            docker run -p 40:80 --name node-app-container ${REGISTRY}:node_app_imagev1.0
            echo "deploy image to a container"
            '''
        }
    }
}
