<img width="2717" height="1506" alt="image" src="https://github.com/user-attachments/assets/4d7534cc-0b9a-49ff-a0ce-5872af4bdbac" />

# REST API's

auth-api: user signup and login
    port: 8005
    database: 3306/taskdb
    apis:
        POST:   http://localhost:8005/auth/signup
        POST:   http://localhost:8005/auth/login
        POST:   http://localhost:8005/auth/logout
        GET:    http://localhost:8005/users/me
        GET:    http://localhost:8005/users/
	POST:	http://localhost:8005/email-verification

finishedProjectDownloader: download finished projects, this also checks if the project is completed
    port: 8011
    database: 3307/filemanager
    apis:
        POST:    http://localhost:8011/project/download

projectFileManager: artists upload projects here
    port: 4000
    database: 3307/filemanager
    apis:
        POST:   http://localhost:4000/api/files/upload
        GET:    http://localhost:4000/api/files/all

projectStatusUpdate: this is a RabbitMQ listener that listens to renderingDoneMessage pushed from renderProgressInfo and updates projectFile->renderingDone=true
    database: 3307/filemanager

renderProgressInfo: checks how many frames are rendered for a particular project. users hit this when they want to know the percentage of their rendered frames
		    this also checks if total frames is equal to the renderedFrames and pushes a message to renderProgressInfo queue to update projectFile->renderingDone=true
    port: 8010
    database: 3309/ongoing-renders
    apis:
        POST:    http://localhost:8010/project/status/frames-rendered

scoringSystem: workers hit this when they render a project and their score is updated.
    port: 8006
    database: 3306/taskdb
    apis:
        POST:   http://localhost:8006/score/worker

stats: return total rendered frames in the whole service week-wise and month-wise. This also listens to a renderStatsQueue to update frames count date-wise
    port: 4009
    database: 3310/statsdb
    apis:
        GET:    http://localhost:4009/stats/general

workerHandler: workers hit this for job request, when finished job - requests for a presigned upload url and hits /worker/job-success. When a job is successful
	       this pushes a message with date to renderStatsQueue to update frames count date-wise
    port: 4001
    database: 3309/ongoing-renders
    apis:
        POST:   http://localhost:4001/api/worker/job-request
        GET:    http://localhost:4001/api/worker/image-upload
        POST:   http://localhost:4001/api/worker/job-success
