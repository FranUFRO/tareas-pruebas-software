import {
  IsArray,
  IsDateString,
  IsInt,
  IsOptional,
  IsString,
  Min,
  ArrayMinSize,
} from 'class-validator';

export class CreatePlanDto {
  @IsArray()
    @ArrayMinSize(1)
    @IsString({ each: true })
    topics: string[] = [];

  @IsInt()
  @Min(1)
  weeks!: number;

  @IsInt()
  @Min(1)
  hoursPerWeek!: number;

  @IsOptional()
  @IsDateString()
  startDate?: string;

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  restrictions?: string[];
}