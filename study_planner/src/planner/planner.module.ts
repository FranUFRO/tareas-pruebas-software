import { Module } from '@nestjs/common';
import { PlannerController } from './planner.controller';
import { PlannerService } from './planner.service';
import { OpenrouterService } from './openrouter.service';

@Module({
  controllers: [PlannerController],
  providers: [PlannerService, OpenrouterService],
})
export class PlannerModule {}