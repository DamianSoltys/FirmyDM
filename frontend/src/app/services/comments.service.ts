import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Subject } from 'rxjs';
import { SearchResponse } from '../classes/Section';
import { OpinionListData } from '../commonComponents/comments/comments.component';

export interface OpinionData {
  comment?:any;
  rating?:any;
  numberOfPages?:number;
}

interface CommentData {
  comment?:string;
  branchId:number;
}

interface RatingData {
  rating?:number;
  branchId:number;
}
@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  constructor(private http:HttpClient) { }

   public postOpinion(opinionData:OpinionListData,rateId?:number):Subject<boolean> {
    let commentData:CommentData = {
      comment:opinionData.comment,
      branchId:opinionData.branchId
    }
    let ratingData:RatingData = {
      rating:opinionData.rate,
      branchId:opinionData.branchId
    }
    console.log(commentData)
    let subject = new Subject<boolean>();
     this.http.post(`http://localhost:8090/api/comment`,commentData).subscribe(response=>{
      if(opinionData.rate) {
        if(rateId) {
          console.log(rateId)
          this.http.patch(`http://localhost:8090/api/rating/${rateId}`,{rating:opinionData.rate}).subscribe(response=>{
            subject.next(true);
          },error=>{
            console.log(error);
            subject.next(false);
          });
        } else {
          this.http.post(`http://localhost:8090/api/rating`,ratingData).subscribe(response=>{
          subject.next(true);
        },error=>{
          console.log(error);
          subject.next(false);
        });
        }
      } else {
        subject.next(true);
      }
     },error=>{
       console.log(error);
       subject.next(false);
     });

     return subject;
  }

  public getOpinion(opinionData:OpinionListData,pageNumber?:number):Subject<OpinionData> {
    let data:OpinionData ={};
    let subject = new Subject<OpinionData>();
    let httpCommentParams:HttpParams;
    if(!pageNumber) {
      httpCommentParams= new HttpParams().set('branchId',opinionData.branchId.toString()).set('size','3');
    } else {
      httpCommentParams= new HttpParams().set('branchId',opinionData.branchId.toString()).set('size','3').set('page',pageNumber.toString());
    }

    this.http.get(`http://localhost:8090/api/comment`,{observe:'response',params:httpCommentParams}).subscribe(response=>{
       let responseData = <SearchResponse>response.body;
       console.log(responseData)
       data.comment = responseData.content;
       data.numberOfPages = responseData.totalPages;
       let httpRatingParams= new HttpParams().set('branchId',opinionData.branchId.toString());
      this.http.get(`http://localhost:8090/api/rating`,{observe:'response',params:httpRatingParams}).subscribe(response=>{
        let responseData = <SearchResponse>response.body;
        data.rating = responseData.content;
        subject.next(data);
      },error=>{
        console.log(error);
        subject.next(null);
      });
     },error=>{
       console.log(error);
       subject.next(null);
     });
    return subject;
  }
}
