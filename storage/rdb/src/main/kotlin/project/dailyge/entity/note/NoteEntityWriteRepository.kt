package project.dailyge.entity.note

interface NoteEntityWriteRepository {
    fun save(note: NoteJpaEntity): Long
}
