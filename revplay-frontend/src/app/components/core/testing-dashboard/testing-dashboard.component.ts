import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TestingService, TestPhase, TestResult } from '../../../services/testing.service';

@Component({
  selector: 'app-testing-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './testing-dashboard.component.html',
  styleUrls: ['./testing-dashboard.component.scss']
})
export class TestingDashboardComponent implements OnInit {
  testPhases: TestPhase[] = [];
  currentPhase: TestPhase | null = null;
  summary: any = {};
  isRunning = false;

  constructor(private testingService: TestingService) {}

  ngOnInit() {
    this.loadTestingData();
  }

  private loadTestingData(): void {
    this.testPhases = this.testingService.getAllPhases();
    this.currentPhase = this.testingService.getCurrentPhase();
    this.summary = this.testingService.getTestingSummary();
  }

  async runCurrentPhaseTests(): Promise<void> {
    this.isRunning = true;
    
    try {
      const results = await this.testingService.runCurrentPhaseTests();
      this.testingService.completeCurrentPhase(results);
      this.loadTestingData();
    } catch (error) {
      console.error('Error running tests:', error);
    } finally {
      this.isRunning = false;
    }
  }

  async runAllTests(): Promise<void> {
    this.testingService.resetTests();
    this.loadTestingData();
    
    while (this.currentPhase && !this.currentPhase.completed) {
      await this.runCurrentPhaseTests();
      
      if (!this.testingService.nextPhase()) {
        break;
      }
      
      this.loadTestingData();
      // Small delay between phases
      await new Promise(resolve => setTimeout(resolve, 1000));
    }
  }

  resetTests(): void {
    this.testingService.resetTests();
    this.loadTestingData();
  }

  getPhaseStatusClass(phase: TestPhase): string {
    if (!phase.completed) return 'pending';
    return phase.passed ? 'passed' : 'failed';
  }

  getPhaseStatusIcon(phase: TestPhase): string {
    if (!phase.completed) return '⏳';
    return phase.passed ? '✅' : '❌';
  }

  getTestStatusClass(test: TestResult): string {
    return test.passed ? 'passed' : 'failed';
  }

  getTestStatusIcon(test: TestResult): string {
    return test.passed ? '✅' : '❌';
  }

  expandPhase(phase: TestPhase): void {
    // Toggle expanded state (would need to implement in component state)
    console.log('Expand phase:', phase.name);
  }

  getProgressPercentage(): number {
    return this.summary.progress || 0;
  }

  isAllTestsComplete(): boolean {
    return this.summary.allPassed || false;
  }

  getCurrentPhaseIndex(): number {
    return this.testPhases.findIndex(phase => phase.name === this.currentPhase?.name);
  }
}
