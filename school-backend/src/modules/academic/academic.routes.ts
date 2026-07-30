import { Router } from 'express';
import { createLookupRouter } from '../../core/lookupFactory';

const router = Router();

router.use('/boards', createLookupRouter({ table: 'boards', fields: ['name'] }));

router.use('/standards', createLookupRouter({ table: 'standards', fields: ['board_id', 'name'], filterableBy: ['board_id'] }));

router.use('/sections', createLookupRouter({ table: 'sections', fields: ['standard_id', 'name'], filterableBy: ['standard_id'] }));

router.use(
  '/subjects',
  createLookupRouter({ table: 'subjects', fields: ['standard_id', 'name', 'code'], filterableBy: ['standard_id'] }),
);

router.use('/batches', createLookupRouter({ table: 'batches', fields: ['name', 'start_date', 'end_date'] }));

router.use('/programs', createLookupRouter({ table: 'programs', fields: ['name'] }));

router.use(
  '/academic-years',
  createLookupRouter({ table: 'academic_years', fields: ['name', 'start_date', 'end_date', 'is_current'] }),
);

router.use('/leave-types', createLookupRouter({ table: 'leave_types', fields: ['name'] }));

router.use('/complaint-types', createLookupRouter({ table: 'complaint_types', fields: ['name'] }));

router.use(
  '/documents-master',
  createLookupRouter({ table: 'documents_master', fields: ['doc_name', 'doc_code', 'is_mandatory'] }),
);

export default router;
