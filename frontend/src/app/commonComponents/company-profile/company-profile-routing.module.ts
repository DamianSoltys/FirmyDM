import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CompanyProfileComponent } from './company-profile.component';
import { AuthGuard } from 'src/app/guards/auth-guard.service';
import { NewsletterComponent } from '../newsletter/newsletter.component';
import { CompanyComponent } from 'src/app/mainComponents/user/company/company.component';
import { NewsletterListComponent } from '../newsletter-list/newsletter-list.component';

const routes: Routes = [
  { path: '', component: CompanyProfileComponent },
  { 
    path: 'newsletter',
    component: NewsletterComponent,
    canActivate: [AuthGuard] },
  { 
    path: 'newsletterList',
    component: NewsletterListComponent,
    canActivate: [AuthGuard] },
  {
    path: 'companyEdit',
    component: CompanyComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'branchEdit',
    component: CompanyComponent,
    canActivate: [AuthGuard]
  },
  { path: '**', redirectTo: 'guest', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CompanyProfileRoutingModule {}
