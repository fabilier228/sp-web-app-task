import { Component, signal } from '@angular/core';
import { MainPageComponent } from './components/main-page/main-page.component';

@Component({
  selector: 'app-root',
  imports: [MainPageComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('frontend');
}
