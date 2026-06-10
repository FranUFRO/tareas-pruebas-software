import { Body, Controller, Get, Post } from '@nestjs/common';
import { CreatePlanDto } from './dto/create_plan.dto';
import { PlannerService } from './planner.service';

@Controller('planner')
export class PlannerController {
  constructor(private readonly plannerService: PlannerService) {}

  @Get('status')
  getStatus() {
    return this.plannerService.getStatus();
  }

  @Post()
  createPlan(@Body() createPlanDto: CreatePlanDto) {
    return this.plannerService.createPlan(createPlanDto);
  }
}