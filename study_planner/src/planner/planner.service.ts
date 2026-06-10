import { Injectable } from '@nestjs/common';
import { CreatePlanDto } from './dto/create_plan.dto';
import { OpenrouterService } from './openrouter.service';

@Injectable()
export class PlannerService {
  constructor(private readonly openrouterService: OpenrouterService) {}

  getStatus() {
    return {
      status: 'ok',
      llm: this.openrouterService.hasApiKey() ? 'configured' : 'not_configured',
    };
  }

  async createPlan(dto: CreatePlanDto) {
    const plan = await this.openrouterService.generatePlan(dto);

    return {
      generatedBy: this.openrouterService.hasApiKey() ? 'llm' : 'local-fallback',
      generatedAt: new Date().toISOString(),
      requestedWeeks: dto.weeks,
      hoursPerWeek: dto.hoursPerWeek,
      weeks: plan,
    };
  }
}