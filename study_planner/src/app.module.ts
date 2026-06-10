import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { PlannerModule } from './planner/planner.module';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
    }),
    PlannerModule,
  ],
})
export class AppModule {}