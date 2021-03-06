import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators
} from '@angular/forms';
import { UserLog, UserREST, UserReg, PersonalData } from '@interfaces/User';
import { LoginService } from '@services/login.service';
import { Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { storage_Avaliable } from '@interfaces/storage_checker';
import { UserService } from '@services/user.service';
import { SnackbarService, SnackbarType } from '@services/snackbar.service';
import { PersonalDataService } from '@services/personal-data.service';
import { FormErrorService } from '@services/form-error.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  public loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private lgservice: LoginService,
    private router: Router,
    private pDataService: PersonalDataService,
    private snackbarService: SnackbarService,
    private formErrorService: FormErrorService
  ) { }

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  get logForm() {
    return this.loginForm.controls;
  }

  public onSubmit() {
    const User_data = this.setUserData();
    this.checkIfLoginSuccess(User_data);
  }

  private checkIfLoginSuccess(User_data: UserLog) {
    this.lgservice.Login(User_data).subscribe(
      (data: HttpResponse<any>) => {
        this.snackbarService.open({
          message: 'Pomyślnie zalogowano',
          snackbarType: SnackbarType.success,
        });

        this.loginStorageSet(data);
        this.pDataService.getPersonalDataObject();
      },
      error => {
        this.formErrorService.open({
          message: 'Coś poszło nie tak!',
        });
      }
    );
  }

  private setUserData(): UserLog {
    let User_data: UserLog = {};
    User_data.email = this.loginForm.get('email').value;
    User_data.password = this.loginForm.get('password').value;

    return User_data;
  }

  private loginStorageSet(data: HttpResponse<any>) {
    if (storage_Avaliable('localStorage')) {
      let userObject: UserREST = data.body;

      localStorage.setItem('token', data.headers.get('Authorization'));
      localStorage.setItem('userREST', JSON.stringify(userObject));

      this.lgservice.ChangeLogged();
      this.router.navigate(['']);
    } else {
      this.formErrorService.open({
        message: 'Coś poszło nie tak!',
      });
    }
  }
}
