package com.poc.util;
def call(Map pipelineParams)
{
    
    SCM_URL='git@github.com:'+pipelineParams.GIT_GROUP+'/'+pipelineParams.REPO+ '.git';
    echo "Code checkout from SCM Repo ${SCM_URL}"
    sh """
    rm -rf ${REPO}
    git clone --single-branch --branch ${BRANCH} ${SCM_URL}
    """
    echo "checkout is completed"
}
