import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { StundenEintrag } from '../services/model/stunden-eintrag';
import { BackendService } from '../services/backend.service';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app-detail-seite',
  templateUrl: './detail-seite.component.html',
  styleUrls: ['./detail-seite.component.css']
})
export class DetailSeiteComponent implements OnInit {

  sub: Subscription;
  id: number;
  eintrag: Observable<StundenEintrag>;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private backendService: BackendService) 
    { }

  ngOnInit() {

    this.sub = this.route
      .queryParams
      .subscribe(params => {
        // Defaults to 0 if no query param provided.
        this.id = +params['id'] || 0;
      });

      this.eintrag = this.backendService.getById(this.id);


  }

}
