export interface ITask{
  id?:number,
  title?:string,
  description?:string,
  team?:any,
  project?:any,
  user?:any,
  parentTask?:string,
  gitHubUrl?:string,
  status?:string,
  completed?:boolean,
  creationDate?:Date,
  modificationDate?:Date,
  deleted?:boolean,
  organisation?:any
}

export class Task implements ITask{
  id:number;
  title:string;
  description:string;
  team:any;
  project:any;
  user:any;
  parentTask:string;
  gitHubUrl:string;
  status:string;
  completed:boolean;
  creationDate:Date;
  modificationDate:Date;
  deleted:boolean;
  organisation:any;

  constructor(id:number,
              title:string,
              description:string,
              team:any,
              project:any,
              user:any,
              parentTask:string,
              gitHubUrl:string,
              status:string,
              completed:boolean,
              creationDate:Date,
              modificationDate:Date,
              deleted:boolean,
              organisation:any) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.team = team;
    this.project = project;
    this.user = user;
    this.parentTask = parentTask;
    this.gitHubUrl = gitHubUrl;
    this.status = status;
    this.completed = completed;
    this.creationDate = creationDate;
    this.modificationDate = modificationDate;
    this.deleted = deleted;
    this.organisation = organisation;
  }
}
