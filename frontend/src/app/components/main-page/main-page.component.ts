import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BackendService } from '../../services/backend.service';
import { phoneContact } from '../../dto/phone-contact.dto';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.scss',
})
export class MainPageComponent implements OnInit {
  private readonly backendService = inject(BackendService);

  contacts = signal<phoneContact[]>([]);
  promptText = signal<string>('');
  isLoading = signal<boolean>(false);
  errorMessage = signal<string | null>(null);
  
  aiMessage = signal<string | null>(null); 

  ngOnInit(): void {
    this.loadContacts();
  }

  loadContacts(): void {
    this.isLoading.set(true);
    this.backendService.getAllContacts().subscribe({
      next: (data) => {
        this.contacts.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Could not fetch contacts from the server.');
        this.isLoading.set(false);
      }
    });
  }

  submitPrompt(): void {
    const currentPrompt = this.promptText().trim();
    if (!currentPrompt) return;

    this.isLoading.set(true);
    this.errorMessage.set(null);
    this.aiMessage.set(null); 

    this.backendService.sendPrompt(currentPrompt).subscribe({
      next: (result: any) => { 
        this.promptText.set('');
        
        if (result && result.response) {
          this.aiMessage.set(result.response);
        } else {
          this.aiMessage.set("Action completed successfully.");
        }
        
        this.loadContacts();
      },
      error: (err) => {
        this.errorMessage.set(err.error?.message || 'An error occurred while processing the command.');
        this.isLoading.set(false);
      }
    });
  }
}